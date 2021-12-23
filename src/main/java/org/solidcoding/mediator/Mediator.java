package org.solidcoding.mediator;

import org.solidcoding.mediator.api.Command;
import org.solidcoding.mediator.api.Event;
import org.solidcoding.mediator.api.Query;

interface Mediator {

  <T extends Command<R>, R> R mediateCommand(T command);

  <T extends Query<R>, R> R mediateQuery(T query);

  <T extends Event> void mediateEvent(T event);

}
