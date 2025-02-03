package com.gabriel.blog.presentation.exceptions;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

import com.gabriel.blog.application.exceptions.ValidationException;
import com.gabriel.blog.domain.exceptions.DomainException;
import com.gabriel.blog.infrastructure.exceptions.RepositoryException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.util.Arrays;

/**
 * Global exception handler class for Quarkus.
 * Maps different types of exceptions and returns custom responses.
 */
@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {

  private final Logger logger = Logger.getLogger(GlobalExceptionHandler.class);

  /**
   * Handles custom and generic exceptions, returning appropriate responses.
   *
   * @param exception The exception thrown by the application.
   * @return Response with the appropriate HTTP status and error message in JSON.
   */
  @Override
  public Response toResponse(final Throwable exception) {

    logger.error(exception.getMessage(), exception);

    return switch (exception) {
      case final RepositoryException repositoryException ->
          Response.status(Response.Status.INTERNAL_SERVER_ERROR)
              .type(MediaType.APPLICATION_JSON)
              .entity(new ErrorResponse("Database error", repositoryException.getMessage(), Arrays.toString(exception.getStackTrace())))
              .build();
      case final DomainException domainException -> Response.status(422)
          .type(MediaType.APPLICATION_JSON)
          .entity(new ErrorResponse("Domain error", domainException.getMessage(), Arrays.toString(exception.getStackTrace())))
          .build();
      case final ValidationException validationException -> Response.status(400)
          .type(MediaType.APPLICATION_JSON)
          .entity(new ErrorResponse("Validation error", validationException.getMessage(), Arrays.toString(exception.getStackTrace())))
          .build();
      default -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .type(MediaType.APPLICATION_JSON)
          .entity(new ErrorResponse("Unexpected error", exception.getMessage(), Arrays.toString(exception.getStackTrace())))
          .build();
    };
  }

  private static class ErrorResponse {
    private String error;
    private String message;
    private String stackTrace;

    public ErrorResponse(final String error, final String message, final String stackTrace) {
      this.error = error;
      this.message = message;
      this.stackTrace = stackTrace;
    }

    public ErrorResponse() {
    }

    public String getError() {
      return error;
    }

    public void setError(final String error) {
      this.error = error;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(final String message) {
      this.message = message;
    }

    public String getStackTrace() {
      return stackTrace;
    }

    public void setStackTrace(final String stackTrace) {
      this.stackTrace = stackTrace;
    }


    @Override
    public String toString() {
      return reflectionToString(this);
    }
  }
}
