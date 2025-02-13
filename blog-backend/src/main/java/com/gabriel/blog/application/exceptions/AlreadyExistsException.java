package com.gabriel.blog.application.exceptions;

/**
 * Represents an exception that occurs when trying to create an entity that already exists.
 *
 * <p>Created by Gabriel Inacio de Souza on February 9, 2025.</p>
 */
public class AlreadyExistsException extends RuntimeException {

  /**
   * Constructs a new {@link AlreadyExistsException} with the provided message.
   *
   * @param message the exception message.
   */
  public AlreadyExistsException(final String message) {
    super(message);
  }
}

