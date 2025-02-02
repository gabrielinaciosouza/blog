package com.gabriel.blog.infrastructure.exceptions;

public class RepositoryException extends RuntimeException {

  public RepositoryException(final String message, final Exception e) {
    super(message, e);
  }
}
