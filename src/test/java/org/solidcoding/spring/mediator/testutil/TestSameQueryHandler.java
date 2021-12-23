package org.solidcoding.spring.mediator.testutil;

import org.solidcoding.spring.mediator.api.QueryHandler;

public class TestSameQueryHandler implements QueryHandler<TestQuery, TestObject> {

  @Override
  public TestObject handle(TestQuery query) {
    SideEffectContext.invoke();
    return null;
  }

}
