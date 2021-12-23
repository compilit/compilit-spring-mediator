package org.solidcoding.mediator.testutil;

import org.solidcoding.mediator.api.EventHandler;

public class TestSameEventHandler implements EventHandler<TestEvent> {

  @Override
  public Void handle(TestEvent command) {
    SideEffectContext.invoke();
    return null;
  }
}
