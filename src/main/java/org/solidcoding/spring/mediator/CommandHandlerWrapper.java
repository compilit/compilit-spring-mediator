package org.solidcoding.spring.mediator;

import org.solidcoding.spring.mediator.api.Command;
import org.solidcoding.spring.mediator.api.CommandHandler;

final class CommandHandlerWrapper {

  private final CommandHandler<?, ?> handler;

  public <C extends Command<R>, R> CommandHandlerWrapper(CommandHandler<C, R> handler) {
    this.handler = handler;
  }

  public <C extends Command<R>, R> CommandHandler<C, R> provideHandler() {
    return (CommandHandler<C, R>) handler;
  }

}