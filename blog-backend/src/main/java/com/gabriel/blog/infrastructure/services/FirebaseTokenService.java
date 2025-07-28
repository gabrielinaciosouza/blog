package com.gabriel.blog.infrastructure.services;

import com.gabriel.blog.application.services.TokenService;
import com.gabriel.blog.domain.valueobjects.Email;
import com.gabriel.blog.infrastructure.exceptions.ServiceException;
import com.google.firebase.auth.FirebaseAuth;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

/**
 * The {@link FirebaseTokenService} is an implementation of the {@link TokenService} interface.
 * It is responsible for decoding and generating tokens using Firebase.
 *
 * <p>Created by Gabriel Inacio de Souza on April 26, 2025.</p>
 *
 * <p>This class is part of the application's service layer,
 * which is responsible for providing various utility functions,
 * including token generation for authentication or other purposes.</p>
 */
@ApplicationScoped
public class FirebaseTokenService implements TokenService {

  private static final Logger LOG = Logger.getLogger(FirebaseTokenService.class);
  private final FirebaseAuth firebaseAuth;

  /**
   * Constructs a new {@link FirebaseTokenService} with the specified
   * {@link FirebaseAuth} instance.
   *
   * @param firebaseAuth the Firebase Authentication instance; must not be {@code null}.
   */
  public FirebaseTokenService(final FirebaseAuth firebaseAuth) {
    this.firebaseAuth = firebaseAuth;
  }

  @Override
  public DecodedToken decode(final String token) {
    try {
      final var decodedToken = firebaseAuth.verifyIdToken(token);
      return new DecodedToken(
          decodedToken.getUid(),
          decodedToken.getEmail(),
          decodedToken.getName(),
          decodedToken.getPicture(),
          decodedToken.getClaims().getOrDefault("role", "USER").toString());
    } catch (final Exception e) {
      LOG.error("Failed to decode token", e);
      throw new ServiceException("Failed to decode token", e);
    }
  }

  @Override
  public String generate(final Email email) {
    try {
      final var userRecord = firebaseAuth.getUserByEmail(email.getEmail());
      return firebaseAuth.createCustomToken(userRecord.getUid());
    } catch (final Exception e) {
      LOG.error("Failed to generate token", e);
      throw new ServiceException("Failed to generate token", e);
    }
  }
}
