package com.gabriel.blog.application.exceptions;

public class ValidationException extends RuntimeException {

  public ValidationException(final String message) {
    super(message);
  }
}
