package com.gabriel.blog.application.services;

import com.gabriel.blog.domain.valueobjects.Email;

/**
 * The {@link TokenGenerator} interface defines the contract for generating tokens.
 * It serves as a marker interface for classes that implement token generation logic.
 *
 * <p>Created by Gabriel Inacio de Souza on April 26, 2025.</p>
 *
 * <p>This interface is part of the application's service layer,
 * which is responsible for providing various utility functions,
 * including token generation for authentication or other purposes.</p>
 */
public interface TokenGenerator {

  /**
   * Generates a token for the specified email.
   *
   * @param email the email address for which to generate the token; must not be {@code null}.
   * @return the generated token as a {@link String}.
   */
  String generate(Email email);
}
