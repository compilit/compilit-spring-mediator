package org.solidcoding.mediator;

import org.solidcoding.mediator.api.Event;
import org.solidcoding.mediator.api.EventHandler;
import org.solidcoding.mediator.api.Request;
import org.solidcoding.mediator.api.RequestHandler;
import org.springframework.context.support.GenericApplicationContext;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.solidcoding.mediator.ExceptionMessages.handlerNotFoundMessage;
import static org.solidcoding.mediator.ExceptionMessages.multipleHandlersRegisteredMessage;
import static org.solidcoding.mediator.GenericTypeParameterResolver.resolveGenericParameters;

abstract class AbstractHandlerProvider {

  private static final int FIRST_TYPE_ARGUMENT = 0;
  protected final GenericApplicationContext genericApplicationContext;

  protected AbstractHandlerProvider(GenericApplicationContext genericApplicationContext) {
    this.genericApplicationContext = genericApplicationContext;
  }

  protected <T> String getHashFor(T requestClass) {
    return UUID.nameUUIDFromBytes(requestClass.getClass().getName().getBytes()).toString();
  }

  protected <T extends Event> void setEventHandlerAtomicReference(AtomicReference<List<EventHandler<T>>> atomicReference,
                                                                  T request) {
    genericApplicationContext
            .getBeanFactory()
            .getBeansOfType(EventHandler.class)
            .forEach((beanName, handler) -> {
                      var generics = resolveGenericParameters(handler, EventHandler.class, beanName);
                      var requestTypeClass = generics.get(FIRST_TYPE_ARGUMENT);
                      if (requestTypeClass.equals(request.getClass())) {
                        atomicReference.get().add(handler);
                      }
                    }
            );
  }

  protected <H extends RequestHandler<T, ?>, T extends Request> void setAtomicReference(
          AtomicReference<H> atomicReference,
          Class<?> handlerClass,
          T request) {
    var handlers = genericApplicationContext.getBeanFactory().getBeansOfType(handlerClass);
    handlers.forEach((beanName, handler) -> {
              List<Class<?>> generics = resolveGenericParameters(handler, handlerClass, beanName);
              var requestTypeClass = generics.get(FIRST_TYPE_ARGUMENT);
              if (requestTypeClass.equals(request.getClass())) {
                if (atomicReference.get() != null) {
                  throw new MediatorException(multipleHandlersRegisteredMessage(request.getClass().getName()));
                }
                atomicReference.set((H) handler);
              }
            }
    );
  }


  protected <T> void makeSureAtomicReferenceIsNotNull(AtomicReference<T> atomicReference, String name) {
    if (atomicReference.get() == null) {
      throw new MediatorException(handlerNotFoundMessage(name));
    }
  }

}
