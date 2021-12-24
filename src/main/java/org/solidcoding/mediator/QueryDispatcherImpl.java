package org.solidcoding.mediator;

import org.solidcoding.mediator.api.Query;
import org.solidcoding.mediator.api.QueryDispatcher;

import javax.annotation.ManagedBean;

@ManagedBean
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
