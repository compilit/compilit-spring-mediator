package org.solidcoding.mediator;

import org.solidcoding.mediator.api.Event;
import org.solidcoding.mediator.api.EventEmitter;

import javax.annotation.ManagedBean;

@ManagedBean
final class EventEmitterImpl implements EventEmitter {

  private final Mediator mediator;

  public EventEmitterImpl(Mediator mediator) {
    this.mediator = mediator;
  }

  @Override
  public void emit(Event event) {
    mediator.mediateEvent(event);
  }

}
