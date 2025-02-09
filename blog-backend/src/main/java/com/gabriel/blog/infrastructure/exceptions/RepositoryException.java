package com.gabriel.blog.infrastructure.exceptions;

/**
 * Custom exception for repository-related errors.
 *
 * <p>This exception is thrown when an error occurs while interacting with a repository,
 * such as a failure to save or retrieve data from the database.
 * It wraps the original exception to provide additional context.
 * </p>
 *
 * <p>Created by Gabriel Inacio de Souza on February 2, 2025.</p>
 */
public class RepositoryException extends RuntimeException {

  /**
   * Constructs a new {@link RepositoryException} with a detailed message and the original cause.
   *
   * @param message the error message describing the failure; must not be {@code null}.
   * @param e       the original exception that caused this error; must not be {@code null}.
   */
  public RepositoryException(final String message, final Exception e) {
    super(message, e);
  }
}
