package org.solidcoding.mediator.testutil;

import org.solidcoding.mediator.api.CommandHandler;

public class TestCommandHandler implements CommandHandler<TestCommand, TestObject> {

  @Override
  public TestObject handle(TestCommand command) {
    SideEffectContext.invoke();
    return null;
  }
}
