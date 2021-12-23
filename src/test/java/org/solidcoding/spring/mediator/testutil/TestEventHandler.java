package org.solidcoding.spring.mediator.testutil;

import org.solidcoding.spring.mediator.api.EventHandler;

public class TestEventHandler implements EventHandler<TestEvent> {

  @Override
  public Void handle(TestEvent event) {
    SideEffectContext.invoke();
    return null;
  }
}
