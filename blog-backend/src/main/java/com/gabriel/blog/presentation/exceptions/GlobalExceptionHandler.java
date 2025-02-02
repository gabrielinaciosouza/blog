package com.gabriel.blog.presentation.exceptions;

import com.gabriel.blog.application.exceptions.ValidationException;
import com.gabriel.blog.domain.exceptions.DomainException;
import com.gabriel.blog.infrastructure.exceptions.RepositoryException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Global exception handler class for Quarkus.
 * Maps different types of exceptions and returns custom responses.
 */
@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {

  /**
   * Handles custom and generic exceptions, returning appropriate responses.
   *
   * @param exception The exception thrown by the application.
   * @return Response with the appropriate HTTP status and error message in JSON.
   */
  @Override
  public Response toResponse(final Throwable exception) {

    return switch (exception) {
      case final RepositoryException repositoryException ->
          Response.status(Response.Status.INTERNAL_SERVER_ERROR)
              .type(MediaType.APPLICATION_JSON)
              .entity(new ErrorResponse("Database error", repositoryException.getMessage()))
              .build();
      case final DomainException domainException -> Response.status(422)
          .type(MediaType.APPLICATION_JSON)
          .entity(new ErrorResponse("Domain error", domainException.getMessage()))
          .build();
      case final ValidationException validationException -> Response.status(400)
          .type(MediaType.APPLICATION_JSON)
          .entity(new ErrorResponse("Validation error", validationException.getMessage()))
          .build();
      default -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .type(MediaType.APPLICATION_JSON)
          .entity(new ErrorResponse("Unexpected error", exception.getMessage()))
          .build();
    };
  }

  /**
   * Internal class to represent standardized error responses.
   * Uses a record for simplicity and immutability.
   *
   * @param error   Type of the error.
   * @param message Detailed error message.
   */
  private record ErrorResponse(String error, String message) {
  }
}
