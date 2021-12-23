package org.solidcoding.spring.mediator.testutil;

import org.solidcoding.spring.mediator.api.CommandHandler;

public class TestSimpleCommandHandler implements CommandHandler<TestSimpleCommand, Void> {

  @Override
  public Void handle(TestSimpleCommand command) {
    SideEffectContext.invoke();
    return null;
  }
}