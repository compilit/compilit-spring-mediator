package org.solidcoding.mediator.testutil;

import org.solidcoding.mediator.api.QueryHandler;

public class TestQueryHandler implements QueryHandler<TestQuery, TestObject> {

  @Override
  public TestObject handle(TestQuery query) {
    SideEffectContext.invoke();
    return null;
  }

}
