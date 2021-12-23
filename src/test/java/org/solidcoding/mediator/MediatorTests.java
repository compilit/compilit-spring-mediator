package org.solidcoding.mediator;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.solidcoding.spring.mediator.api.*;
import org.solidcoding.spring.mediator.testutil.*;
import org.solidcoding.mediator.api.*;
import org.solidcoding.mediator.testutil.*;
import org.springframework.context.support.GenericApplicationContext;

public class MediatorTests {

  private final GenericApplicationContext context = new GenericApplicationContext();

  @BeforeEach
  public void reset() {
    SideEffectContext.reset();
  }

  @Test
  public void dispatch_MultipleEqualHandlers_ShouldThrowException() {
    context.refresh();
    context.registerBean(TestCommandHandler.class.getName(), CommandHandler.class, () -> new TestCommandHandler());
    TestApplicationContext.registerCqersModule(context);
    var commandDispatcher = context.getBean(CommandDispatcher.class);
    Assertions.assertThatNoException().isThrownBy(() -> commandDispatcher.dispatch(new TestCommand()));
  }

  @Test
  public void emit_Event_ShouldEmitEvent() {
    context.refresh();
    AssertionsForClassTypes.assertThat(SideEffectContext.isInvoked).isFalse();
    context.registerBean(EventHandler.class, TestEventHandler::new);
    TestApplicationContext.registerCqersModule(context);
    var emitter = context.getBean(EventEmitter.class);
    emitter.emit(new TestEvent());
    Assertions.assertThat(SideEffectContext.isInvoked(1)).isTrue();
  }

  @Test
  public void emit_EventWithMultipleEventHandlersOfTheSameEvent_ShouldHandleEventMultipleTimes() {
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
  public void dispatch_Command_ShouldDispatchCommand() {
    context.refresh();
    AssertionsForClassTypes.assertThat(SideEffectContext.isInvoked).isFalse();
    context.registerBean(CommandHandler.class, TestCommandHandler::new);
    TestApplicationContext.registerCqersModule(context);
    var dispatcher = context.getBean(CommandDispatcher.class);
    dispatcher.dispatch(new TestCommand());
    AssertionsForClassTypes.assertThat(SideEffectContext.isInvoked).isTrue();
  }

  @Test
  public void dispatch_MultipleEqualCommands_ShouldThrowException() {
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
  public void dispatch_SimpleCommand_ShouldDispatchSimpleCommand() {
    context.refresh();
    AssertionsForClassTypes.assertThat(SideEffectContext.isInvoked).isFalse();
    context.registerBean(CommandHandler.class, TestSimpleCommandHandler::new);
    TestApplicationContext.registerCqersModule(context);
    var dispatcher = context.getBean(CommandDispatcher.class);
    dispatcher.dispatch(new TestSimpleCommand());
    AssertionsForClassTypes.assertThat(SideEffectContext.isInvoked).isTrue();
  }

  @Test
  public void dispatch_Query_ShouldDispatchQuery() {
    context.refresh();
    AssertionsForClassTypes.assertThat(SideEffectContext.isInvoked).isFalse();
    context.registerBean(QueryHandler.class, TestQueryHandler::new);
    TestApplicationContext.registerCqersModule(context);
    var dispatcher = context.getBean(QueryDispatcher.class);
    dispatcher.dispatch(new TestQuery());
    AssertionsForClassTypes.assertThat(SideEffectContext.isInvoked).isTrue();
  }

  @Test
  public void dispatch_MultipleEqualQueries_ShouldDispatchQuery() {
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
  public void multipleInvocations_ShouldWork() {
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
