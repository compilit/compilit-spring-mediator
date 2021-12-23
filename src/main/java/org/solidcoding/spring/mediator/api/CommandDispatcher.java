package org.solidcoding.spring.mediator.api;

/**
 * A CommandDispatcher is a dedicated interface for sending Commands to the Mediator.
 * A Mediator should never be interacted with directly because you could never truly know that your code complies with CQERS.
 * @see Command
 */
public interface CommandDispatcher {

  /**
   * Dispatch the Command to the Mediator in order to handle it.
   *
   * @param command The specific Command you wish to send to the Mediator.
   * @param <T> The return type of the Command.
   * @return The return type value.
   */
  <T> T dispatch(Command<T> command);
}
