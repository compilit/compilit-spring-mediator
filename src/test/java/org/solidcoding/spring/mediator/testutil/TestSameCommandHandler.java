package org.solidcoding.spring.mediator.testutil;

import org.solidcoding.spring.mediator.api.CommandHandler;

public class TestSameCommandHandler implements CommandHandler<TestCommand, TestObject> {

  @Override
  public TestObject handle(TestCommand command) {
    SideEffectContext.invoke();
    return null;
  }
}