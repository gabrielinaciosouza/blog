package com.gabriel.blog.infrastructure.exceptions;

/**
 * Custom exception for service-related errors.
 *
 * <p>This exception is thrown when an error occurs while interacting with a service.
 * It wraps the original exception to provide additional context.
 * </p>
 *
 * <p>Created by Gabriel Inacio de Souza on February 2, 2025.</p>
 */
public class ServiceException extends RuntimeException {

  /**
   * Constructs a new {@link ServiceException} with a detailed message and the original cause.
   *
   * @param message the error message describing the failure; must not be {@code null}.
   * @param e       the original exception that caused this error; must not be {@code null}.
   */
  public ServiceException(final String message, final Exception e) {
    super(message, e);
  }

  /**
   * Constructs a new {@link ServiceException} with a detailed message.
   *
   * @param message the error message describing the failure; must not be {@code null}.
   */
  public ServiceException(final String message) {
    super(message);
  }
}
