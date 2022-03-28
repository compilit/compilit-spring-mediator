package com.compilit.mediator;

import com.compilit.mediator.api.Command;
import com.compilit.mediator.api.CommandDispatcher;

import javax.annotation.ManagedBean;

@ManagedBean
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
