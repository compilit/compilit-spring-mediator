package org.solidcoding.spring.mediator.testutil;

import org.solidcoding.spring.mediator.api.EventHandler;

public class TestSameEventHandler implements EventHandler<TestEvent> {

  @Override
  public Void handle(TestEvent command) {
    SideEffectContext.invoke();
    return null;
  }
}
