package com.gabriel.blog.presentation.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabriel.blog.application.requests.AuthenticationRequest;
import com.gabriel.blog.application.responses.AuthenticationResponse;
import com.gabriel.blog.application.usecases.AuthenticationUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuthenticationResourceTest {

  private AuthenticationUseCase authenticationUseCase;
  private AuthenticationResource authenticationResource;

  @BeforeEach
  void setUp() {
    authenticationUseCase = mock(AuthenticationUseCase.class);
    authenticationResource = new AuthenticationResource(authenticationUseCase);
  }

  @Test
  void authenticateWithGoogleSuccessfully() {
    final var idToken = "test-id-token";
    final var request = new AuthenticationRequest(idToken);
    final var expectedResponse = mock(AuthenticationResponse.class);

    when(authenticationUseCase.authenticateWithGoogle(idToken)).thenReturn(expectedResponse);

    final var result = authenticationResource.authenticate(request);

    assertEquals(expectedResponse, result);
    verify(authenticationUseCase).authenticateWithGoogle(idToken);
  }
}

