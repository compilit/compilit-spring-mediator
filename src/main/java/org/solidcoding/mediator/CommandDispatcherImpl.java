package org.solidcoding.mediator;

import org.solidcoding.mediator.api.Command;
import org.solidcoding.mediator.api.CommandDispatcher;
import org.springframework.stereotype.Component;

@Component
final class CommandDispatcherImpl implements CommandDispatcher {

  private final Mediator mediator;

  public CommandDispatcherImpl(Mediator mediator) {
    this.mediator = mediator;
  }

  @Override
  public <T> T dispatch(Command<T> command) {
    return mediator.mediateCommand(command);
  }

}
