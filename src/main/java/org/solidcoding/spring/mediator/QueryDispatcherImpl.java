package org.solidcoding.spring.mediator;

import org.solidcoding.spring.mediator.api.Query;
import org.solidcoding.spring.mediator.api.QueryDispatcher;
import org.springframework.stereotype.Component;

@Component
final class QueryDispatcherImpl implements QueryDispatcher {

  private final Mediator mediator;

  public QueryDispatcherImpl(Mediator mediator) {
    this.mediator = mediator;
  }

  @Override
  public <T> T dispatch(Query<T> query) {
    return mediator.mediateQuery(query);
  }

}
