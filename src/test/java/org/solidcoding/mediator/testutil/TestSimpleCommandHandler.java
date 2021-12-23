package org.solidcoding.mediator.testutil;

import org.solidcoding.mediator.api.CommandHandler;

public class TestSimpleCommandHandler implements CommandHandler<TestSimpleCommand, Void> {

  @Override
  public Void handle(TestSimpleCommand command) {
    SideEffectContext.invoke();
    return null;
  }
}