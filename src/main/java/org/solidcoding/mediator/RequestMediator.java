package org.solidcoding.mediator;

import org.solidcoding.mediator.api.*;

import javax.annotation.ManagedBean;
import java.util.List;

@ManagedBean
final class RequestMediator implements Mediator {

  private final CommandHandlerProvider commandHandlerProvider;
  private final QueryHandlerProvider queryHandlerProvider;
  private final EventHandlerProvider eventHandlerProvider;

  public RequestMediator(CommandHandlerProvider commandHandlerProvider,
                         QueryHandlerProvider queryHandlerProvider,
                         EventHandlerProvider eventHandlerProvider) {
    this.commandHandlerProvider = commandHandlerProvider;
    this.queryHandlerProvider = queryHandlerProvider;
    this.eventHandlerProvider = eventHandlerProvider;
  }

  @Override
  public <T extends Command<R>, R> R mediateCommand(T command) {
    CommandHandler<Command<R>, R> handler = commandHandlerProvider.getCommandHandler(command);
    return handler.handle(command);
  }

  @Override
  public <T extends Query<R>, R> R mediateQuery(T query) {
    QueryHandler<Query<R>, R> handler = queryHandlerProvider.getQueryHandler(query);
    return handler.handle(query);
  }

  @Override
  public <T extends Event> void mediateEvent(T event) {
    List<EventHandler<Event>> handlers = eventHandlerProvider.getEventHandlers(event);
    for (var handler : handlers) {
      handler.handle(event);
    }
  }

}
