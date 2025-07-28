package com.gabriel.blog.presentation.resources;

import com.gabriel.blog.application.requests.AuthenticationRequest;
import com.gabriel.blog.application.responses.AuthenticationResponse;
import com.gabriel.blog.application.usecases.AuthenticationUseCase;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

/**
 * The {@link AuthenticationResource} class provides RESTful endpoints for user authentication.
 * It handles requests to authenticate users using Google OAuth tokens.
 *
 * <p>Created by Gabriel Inacio de Souza on June 12, 2025.</p>
 **/
@Path("/auth")
@Consumes("application/json")
@Produces("application/json")
public class AuthenticationResource {

  private final AuthenticationUseCase authenticationUseCase;

  /**
   * Constructs {@link AuthenticationResource} with the specified {@link AuthenticationUseCase}.
   *
   * @param authenticationUseCase use case for handling authentication; must not be {@code null}.
   */
  public AuthenticationResource(final AuthenticationUseCase authenticationUseCase) {
    this.authenticationUseCase = authenticationUseCase;
  }

  /**
   * Authenticates a user using a Google OAuth token.
   *
   * @param request the authentication request containing the Google ID token.
   * @return an {@link AuthenticationResponse} containing user details and access token.
   */
  @POST
  @Path("/google")
  public AuthenticationResponse authenticate(final AuthenticationRequest request) {
    return authenticationUseCase.authenticateWithGoogle(request.idToken());
  }

}
