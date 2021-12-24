package org.solidcoding.mediator;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.solidcoding.mediator.api.*;
import org.solidcoding.mediator.testutil.*;
import org.springframework.context.support.GenericApplicationContext;

class MediatorTests {

  private final GenericApplicationContext context = new GenericApplicationContext();

  @BeforeEach
  void reset() {
    SideEffectContext.reset();
  }

  @Test
  void dispatch_multipleEqualHandlers_shouldThrowException() {
    context.refresh();
    context.registerBean(TestCommandHandler.class.getName(), CommandHandler.class, () -> new TestCommandHandler());
    TestApplicationContext.registerCqersModule(context);
    var commandDispatcher = context.getBean(CommandDispatcher.class);
    Assertions.assertThatNoException().isThrownBy(() -> commandDispatcher.dispatch(new TestCommand()));
  }

  @Test
  void emit_event_shouldInteractWithContext() {
    context.refresh();
    AssertionsForClassTypes.assertThat(SideEffectContext.isInvoked).isFalse();
    context.registerBean(EventHandler.class, TestEventHandler::new);
    TestApplicationContext.registerCqersModule(context);
    var emitter = context.getBean(EventEmitter.class);
    emitter.emit(new TestEvent());
    Assertions.assertThat(SideEffectContext.isInvoked(1)).isTrue();
  }

  @Test
  void emit_eventWithMultipleEventHandlersOfTheSameEvent_shouldInteractWithContextMultipleTimes() {
    context.refresh();
    AssertionsForClassTypes.assertThat(SideEffectContext.isInvoked).isFalse();
    context.registerBean("1", EventHandler.class, TestEventHandler::new);
    context.registerBean("2", EventHandler.class, TestSameEventHandler::new);
    TestApplicationContext.registerCqersModule(context);
    var emitter = context.getBean(EventEmitter.class);
    emitter.emit(new TestEvent());
    AssertionsForClassTypes.assertThat(SideEffectContext.isInvoked(2)).isTrue();
  }

  @Test
  void dispatch_command_shouldDispatchCommand() {
    context.refresh();
    AssertionsForClassTypes.assertThat(SideEffectContext.isInvoked).isFalse();
    context.registerBean(CommandHandler.class, TestCommandHandler::new);
    TestApplicationContext.registerCqersModule(context);
    var dispatcher = context.getBean(CommandDispatcher.class);
    dispatcher.dispatch(new TestCommand());
    AssertionsForClassTypes.assertThat(SideEffectContext.isInvoked).isTrue();
  }

  @Test
  void dispatch_multipleEqualCommands_shouldThrowException() {
    context.refresh();
    AssertionsForClassTypes.assertThat(SideEffectContext.isInvoked).isFalse();
    context.registerBean("1", CommandHandler.class, TestCommandHandler::new);
    context.registerBean("2", CommandHandler.class, TestSameCommandHandler::new);
    TestApplicationContext.registerCqersModule(context);
    var dispatcher = context.getBean(CommandDispatcher.class);
    Assertions.assertThatThrownBy(() -> dispatcher.dispatch(new TestCommand()))
            .isInstanceOf(MediatorException.class)
            .hasMessage(ExceptionMessages.multipleHandlersRegisteredMessage(TestCommand.class.getName()));
  }

  @Test
  void dispatch_simpleCommand_shouldInteractWithContext() {
    context.refresh();
    AssertionsForClassTypes.assertThat(SideEffectContext.isInvoked).isFalse();
    context.registerBean(CommandHandler.class, TestSimpleCommandHandler::new);
    TestApplicationContext.registerCqersModule(context);
    var dispatcher = context.getBean(CommandDispatcher.class);
    dispatcher.dispatch(new TestSimpleCommand());
    AssertionsForClassTypes.assertThat(SideEffectContext.isInvoked).isTrue();
  }

  @Test
  void dispatch_query_shouldInteractWithContext() {
    context.refresh();
    AssertionsForClassTypes.assertThat(SideEffectContext.isInvoked).isFalse();
    context.registerBean(QueryHandler.class, TestQueryHandler::new);
    TestApplicationContext.registerCqersModule(context);
    var dispatcher = context.getBean(QueryDispatcher.class);
    dispatcher.dispatch(new TestQuery());
    AssertionsForClassTypes.assertThat(SideEffectContext.isInvoked).isTrue();
  }

  @Test
  void dispatch_multipleEqualQueries_shouldThrowException() {
    context.refresh();
    AssertionsForClassTypes.assertThat(SideEffectContext.isInvoked).isFalse();
    context.registerBean("1", QueryHandler.class, TestQueryHandler::new);
    context.registerBean("2", QueryHandler.class, TestSameQueryHandler::new);
    TestApplicationContext.registerCqersModule(context);
    var dispatcher = context.getBean(QueryDispatcher.class);
    Assertions.assertThatThrownBy(() -> dispatcher.dispatch(new TestQuery()))
            .isInstanceOf(MediatorException.class)
            .hasMessage(ExceptionMessages.multipleHandlersRegisteredMessage(TestQuery.class.getName()));
  }

  @Test
  void dispatch_multipleInvocations_shouldInteractWithContextMultipleTimes() {
    context.refresh();
    AssertionsForClassTypes.assertThat(SideEffectContext.isInvoked).isFalse();
    context.registerBean(QueryHandler.class, TestQueryHandler::new);
    context.registerBean("1", CommandHandler.class, TestCommandHandler::new);
    context.registerBean("2", CommandHandler.class, TestSimpleCommandHandler::new);
    context.registerBean(EventHandler.class, TestEventHandler::new);
    TestApplicationContext.registerCqersModule(context);
    var queryDispatcher = context.getBean(QueryDispatcher.class);
    var commandDispatcher = context.getBean(CommandDispatcher.class);
    var eventEmitter = context.getBean(EventEmitter.class);
    queryDispatcher.dispatch(new TestQuery());
    commandDispatcher.dispatch(new TestSimpleCommand());
    commandDispatcher.dispatch(new TestCommand());
    eventEmitter.emit(new TestEvent());
    AssertionsForClassTypes.assertThat(SideEffectContext.isInvoked(4)).isTrue();
  }

}
