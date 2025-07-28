package com.gabriel.blog.presentation.filters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabriel.blog.application.services.TokenService;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class FirebaseAuthFilterTest {

  private TokenService tokenService;
  private FirebaseAuthFilter filter;
  private ContainerRequestContext requestContext;

  @BeforeEach
  void setUp() {
    tokenService = mock(TokenService.class);
    filter = new FirebaseAuthFilter(tokenService);
    requestContext = mock(ContainerRequestContext.class);
  }

  @Test
  void shouldAbortIfNoAuthorizationHeader() {
    when(requestContext.getHeaderString("Authorization")).thenReturn(null);
    filter.filter(requestContext);
    verify(requestContext).abortWith(any(Response.class));
  }

  @Test
  void shouldAbortIfAuthorizationHeaderIsInvalid() {
    when(requestContext.getHeaderString("Authorization")).thenReturn("InvalidHeader");
    filter.filter(requestContext);
    verify(requestContext).abortWith(any(Response.class));
  }

  @Test
  void shouldAbortIfTokenServiceThrows() {
    when(requestContext.getHeaderString("Authorization")).thenReturn("Bearer token");
    when(tokenService.decode("token")).thenThrow(new RuntimeException("fail"));
    filter.filter(requestContext);
    verify(requestContext).abortWith(any(Response.class));
  }

  @Test
  void shouldAbortIfRoleIsNotAdmin() {
    when(requestContext.getHeaderString("Authorization")).thenReturn("Bearer token");
    final var decodedToken = new TokenService.DecodedToken("id", "email", "name", "img", "USER");
    when(tokenService.decode("token")).thenReturn(decodedToken);
    filter.filter(requestContext);
    verify(requestContext).abortWith(any(Response.class));
  }

  @Test
  void shouldSetDecodedTokenAndAllowIfAdmin() {
    when(requestContext.getHeaderString("Authorization")).thenReturn("Bearer token");
    final var decodedToken = new TokenService.DecodedToken("id", "email", "name", "img", "ADMIN");
    when(tokenService.decode("token")).thenReturn(decodedToken);
    filter.filter(requestContext);
    verify(requestContext).setProperty("decodedToken", decodedToken);
    verify(requestContext, never()).abortWith(any(Response.class));
  }

  @Test
  void shouldReturnUnauthorizedResponseOnAbort() {
    // Use ArgumentCaptor to check the response status
    ArgumentCaptor<Response> captor = ArgumentCaptor.forClass(Response.class);
    filter.abort(requestContext);
    verify(requestContext).abortWith(captor.capture());
    assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), captor.getValue().getStatus());
  }
}

