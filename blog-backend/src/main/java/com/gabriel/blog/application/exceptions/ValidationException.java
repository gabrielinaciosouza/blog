package com.gabriel.blog.application.exceptions;

/**
 * Exception thrown when a validation error occurs.
 * This exception is used to indicate that some input data does not meet the required constraints.
 */
public class ValidationException extends RuntimeException {

  /**
   * Constructs a new ValidationException with the specified detail message.
   *
   * @param message The detail message explaining the validation error.
   */
  public ValidationException(final String message) {
    super(message);
  }
}

