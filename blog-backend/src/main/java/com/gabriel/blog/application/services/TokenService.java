package com.gabriel.blog.application.services;

import com.gabriel.blog.domain.valueobjects.Email;

/**
 * The {@link TokenService} interface defines the contract for generating tokens.
 * It serves as a marker interface for classes that implement token generation logic.
 *
 * <p>Created by Gabriel Inacio de Souza on April 26, 2025.</p>
 *
 * <p>This interface is part of the application's service layer,
 * which is responsible for providing various utility functions,
 * including token generation for authentication or other purposes.</p>
 */
public interface TokenService {

  /**
   * Decodes a token and returns the decoded information.
   *
   * @param token the token to decode; must not be {@code null}.
   * @return a {@link DecodedToken} object containing the decoded information.
   */
  DecodedToken decode(String token);

  /**
   * Generates a token for the given email.
   *
   * @param email the email to generate a token for; must not be {@code null}.
   * @return the generated token as a {@link String}.
   */
  String generate(Email email);

  /**
   * Represents a decoded token.
   */
  record DecodedToken(
      String id,
      String email,
      String name,
      String pictureUrl,
      String role) {

  }
}
