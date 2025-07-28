package com.gabriel.blog.presentation.filters;

import com.gabriel.blog.application.services.TokenService;
import com.gabriel.blog.domain.entities.User;
import io.quarkus.arc.profile.IfBuildProfile;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

/**
 * The {@link FirebaseAuthFilter} class is a JAX-RS filter that intercepts incoming requests
 * to authenticate users using Firebase tokens.
 *
 * <p>Created by Gabriel Inacio de Souza on June 12, 2025.</p>
 */
@Provider
@Secured
@IfBuildProfile(allOf = { "dev", "prod" })
public class FirebaseAuthFilter implements ContainerRequestFilter {

  private static final Logger logger = Logger.getLogger(FirebaseAuthFilter.class);

  private final TokenService tokenService;

  /**
   * Constructs a new {@link FirebaseAuthFilter} with the specified {@link TokenService}.
   *
   * @param tokenService the service used to decode Firebase tokens; must not be {@code null}.
   */
  public FirebaseAuthFilter(final TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @Override
  public void filter(final ContainerRequestContext requestContext) {
    final var authHeader = requestContext.getHeaderString("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      abort(requestContext);
      return;
    }

    final var token = authHeader.substring("Bearer ".length());

    try {
      final var decodedToken = tokenService.decode(token);
      logger.info("Decoded token: " + decodedToken);
      requestContext.setProperty("decodedToken", decodedToken);

      if (!User.Role.valueOf(decodedToken.role().toUpperCase()).equals(User.Role.ADMIN)) {
        abort(requestContext);
      }
    } catch (final Exception e) {
      abort(requestContext);
    }
  }


  void abort(final ContainerRequestContext ctx) {
    ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
  }
}
