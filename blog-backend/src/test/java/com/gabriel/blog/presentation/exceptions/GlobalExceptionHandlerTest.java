package com.gabriel.blog.presentation.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.gabriel.blog.application.exceptions.AlreadyExistsException;
import com.gabriel.blog.application.exceptions.NotFoundException;
import com.gabriel.blog.domain.exceptions.DomainException;
import com.gabriel.blog.infrastructure.exceptions.RepositoryException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

class GlobalExceptionHandlerTest {

  private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

  @Test
  void shouldHandleRepositoryException() {
    final var repositoryException = mock(RepositoryException.class);
    when(repositoryException.getMessage()).thenReturn("Database connection failed");

    final var response = exceptionHandler.toResponse(repositoryException);

    assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    assertTrue(response.getEntity().toString().contains("Database error"));
    assertTrue(response.getEntity().toString().contains("Database connection failed"));
  }

  @Test
  void shouldHandleDomainException() {
    final var domainException = mock(DomainException.class);
    when(domainException.getMessage()).thenReturn("Invalid business rule");

    final var response = exceptionHandler.toResponse(domainException);

    assertEquals(422, response.getStatus());
    assertTrue(response.getEntity().toString().contains("Domain error"));
    assertTrue(response.getEntity().toString().contains("Invalid business rule"));
  }

  @Test
  void shouldHandleNotFoundException() {
    final var notFoundException = new NotFoundException("Resource not found");

    final var response = exceptionHandler.toResponse(notFoundException);

    assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    assertTrue(response.getEntity().toString().contains("Not found"));
  }

  @Test
  void shouldHandleAlreadyExistsException() {
    final var alreadyExistsException = new AlreadyExistsException("Resource already exists");

    final var response = exceptionHandler.toResponse(alreadyExistsException);

    assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus());
    assertTrue(response.getEntity().toString().contains("Already exists"));
  }

  @Test
  void testHandleGenericException() {
    final var exception = new Throwable("Unexpected error occurred");

    final var response = exceptionHandler.toResponse(exception);

    assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    assertTrue(response.getEntity().toString().contains("Unexpected error"));
    assertTrue(response.getEntity().toString().contains("Unexpected error occurred"));
  }
}
