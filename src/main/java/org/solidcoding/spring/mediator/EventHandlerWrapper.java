package org.solidcoding.spring.mediator;

import org.solidcoding.spring.mediator.api.Event;
import org.solidcoding.spring.mediator.api.EventHandler;

import java.util.ArrayList;
import java.util.List;

final class EventHandlerWrapper {

  private final List<EventHandler<Event>> handlers = new ArrayList<>();

  public EventHandlerWrapper(List<EventHandler<Event>> handlers) {
    this.handlers.addAll(handlers);
  }

  public List<EventHandler<Event>> provideHandler() {
    return handlers;
  }

}
