package org.solidcoding.mediator;

import org.solidcoding.mediator.api.Event;
import org.solidcoding.mediator.api.EventHandler;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Component
final class EventHandlerProvider extends AbstractHandlerProvider {

  private final Map<String, EventHandlerWrapper> eventHandlerMap = new HashMap<>();

  public EventHandlerProvider(GenericApplicationContext genericApplicationContext) {
    super(genericApplicationContext);
  }

  public List<EventHandler<Event>> getEventHandlers(Event requestClass) {
    try {
      var hash = getHashFor(requestClass);
      if (!eventHandlerMap.containsKey(hash)) {
        var eventHandlers = findEventHandler(requestClass);
        eventHandlerMap.put(hash, new EventHandlerWrapper(eventHandlers));
      }
      var provider = eventHandlerMap.get(hash);
      return provider.provideHandler();
    } catch (Exception exception) {
      throw new MediatorException(exception.getMessage());
    }
  }

  private <T extends Event> List<EventHandler<T>> findEventHandler(T requestClass) {
    var atomicReference = new AtomicReference<List<EventHandler<T>>>();
    atomicReference.set(new ArrayList<>());
    setEventHandlerAtomicReference(atomicReference, requestClass);
    makeSureAtomicReferenceIsNotNull(atomicReference, requestClass.getClass().getName());
    return atomicReference.get();
  }

}
