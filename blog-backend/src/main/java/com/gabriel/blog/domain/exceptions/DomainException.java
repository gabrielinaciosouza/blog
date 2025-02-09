package com.gabriel.blog.domain.exceptions;

/**
 * Represents a domain-specific runtime exception.
 * This exception is used to indicate violations of domain rules or constraints.
 *
 * <p>Created by Gabriel Inacio de Souza on February 1, 2025.</p>
 */
public class DomainException extends RuntimeException {

  /**
   * Constructs a new {@code DomainException} with the specified detail message.
   *
   * @param message the detail message explaining the reason for the exception
   */
  public DomainException(final String message) {
    super(message);
  }
}

