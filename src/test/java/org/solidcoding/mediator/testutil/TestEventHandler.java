package org.solidcoding.mediator.testutil;

import org.solidcoding.mediator.api.EventHandler;

public class TestEventHandler implements EventHandler<TestEvent> {

  @Override
  public Void handle(TestEvent event) {
    SideEffectContext.invoke();
    return null;
  }
}
