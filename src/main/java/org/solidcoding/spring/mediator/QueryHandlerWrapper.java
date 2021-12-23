package org.solidcoding.spring.mediator;

import org.solidcoding.spring.mediator.api.Query;
import org.solidcoding.spring.mediator.api.QueryHandler;

final class QueryHandlerWrapper {

  private final QueryHandler<?, ?> handler;

  public <R> QueryHandlerWrapper(QueryHandler<? extends Query<R>, R> handler) {
    this.handler = handler;
  }

  public <C extends Query<R>, R> QueryHandler<C, R> provideHandler() {
    return (QueryHandler<C, R>) handler;
  }

}
