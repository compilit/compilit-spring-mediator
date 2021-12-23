package org.solidcoding.spring.mediator;

import org.solidcoding.spring.mediator.api.CommandDispatcher;
import org.solidcoding.spring.mediator.api.EventEmitter;
import org.solidcoding.spring.mediator.api.QueryDispatcher;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Objects;

public class TestApplicationContext {

  private static final String IOC_CONTAINER_NOT_AVAILABLE = "genericApplicationContext not available";

  public static GenericApplicationContext registerCqersModule(GenericApplicationContext genericApplicationContext) {
    Objects.requireNonNull(genericApplicationContext, IOC_CONTAINER_NOT_AVAILABLE);
    registerHandlerProviders(genericApplicationContext);
    registerMediator(genericApplicationContext);
    registerDispatchers(genericApplicationContext);
    return genericApplicationContext;
  }

  private static void registerDispatchers(GenericApplicationContext genericApplicationContext) {
    var mediator = genericApplicationContext.getBean(Mediator.class);
    genericApplicationContext.registerBean(CommandDispatcher.class,
            () -> new CommandDispatcherImpl(mediator));
    genericApplicationContext.registerBean(QueryDispatcher.class, () -> new QueryDispatcherImpl(mediator));
    genericApplicationContext.registerBean(EventEmitter.class, () -> new EventEmitterImpl(mediator));
  }

  private static void registerMediator(GenericApplicationContext genericApplicationContext) {
    var commandHandlerProvider = genericApplicationContext.getBean(CommandHandlerProvider.class);
    var queryHandlerProvider = genericApplicationContext.getBean(QueryHandlerProvider.class);
    var eventHandlerProvider = genericApplicationContext.getBean(EventHandlerProvider.class);
    genericApplicationContext.registerBean(Mediator.class,
            () -> new RequestMediator(commandHandlerProvider,
                    queryHandlerProvider,
                    eventHandlerProvider));
  }

  private static void registerHandlerProviders(GenericApplicationContext genericApplicationContext) {
    genericApplicationContext.registerBean(CommandHandlerProvider.class,
            () -> new CommandHandlerProvider(genericApplicationContext));
    genericApplicationContext.registerBean(QueryHandlerProvider.class,
            () -> new QueryHandlerProvider(genericApplicationContext));
    genericApplicationContext.registerBean(EventHandlerProvider.class,
            () -> new EventHandlerProvider(genericApplicationContext));
  }

}
