package org.solidcoding.spring.mediator;

import org.solidcoding.spring.mediator.api.Command;
import org.solidcoding.spring.mediator.api.Event;
import org.solidcoding.spring.mediator.api.Query;

interface Mediator {

  <T extends Command<R>, R> R mediateCommand(T command);

  <T extends Query<R>, R> R mediateQuery(T query);

  <T extends Event> void mediateEvent(T event);

}
