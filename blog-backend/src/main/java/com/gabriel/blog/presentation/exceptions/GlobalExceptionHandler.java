package com.gabriel.blog.presentation.exceptions;

import com.gabriel.blog.application.exceptions.NotFoundException;
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
          buildResponse("Database error", repositoryException.getMessage(),
              Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
      case final DomainException domainException -> buildResponse("Domain error",
          domainException.getMessage(), 422);
      case final ValidationException validationException -> buildResponse("Validation error",
          validationException.getMessage(), Response.Status.BAD_REQUEST.getStatusCode());
      case final NotFoundException notFoundException -> buildResponse("Not found",
          notFoundException.getMessage(), Response.Status.NOT_FOUND.getStatusCode());
      default -> buildResponse("Internal error", exception.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
    };
  }

  private Response buildResponse(final String error, final String message,
                                 final int status) {
    return Response.status(status)
        .type(MediaType.APPLICATION_JSON)
        .entity(new ErrorResponse(error, message))
        .build();
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
