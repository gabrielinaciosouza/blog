package com.gabriel.blog.application.exceptions;

/**
 * Represents an exception thrown when a requested resource is not found.
 *
 * <p>Created by Gabriel Inacio de Souza on February 9, 2025.</p>
 */
public class NotFoundException extends RuntimeException {

  /**
   * Constructs a new {@code NotFoundException} with the given message.
   *
   * @param message the exception message
   */
  public NotFoundException(final String message) {
    super(message);
  }
}

