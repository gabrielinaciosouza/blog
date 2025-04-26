package com.gabriel.blog.application.usecases;

import com.gabriel.blog.application.repositories.UserRepository;
import com.gabriel.blog.application.responses.AuthenticationResponse;
import com.gabriel.blog.application.services.TokenGenerator;
import com.gabriel.blog.domain.valueobjects.Email;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * This class is responsible for handling authorization-related use cases.
 * It contains methods to check user permissions and roles.
 */
@ApplicationScoped
public class AuthenticationUseCase {

  final UserRepository userRepository;
  final TokenGenerator tokenGenerator;

  /**
   * Constructs a new {@link AuthenticationUseCase} with the specified {@link UserRepository}.
   * This constructor allows the injection of the repository that will be responsible for user
   * operations.
   *
   * @param userRepository the repository that handles user operations; must not be {@code null}.
   */
  public AuthenticationUseCase(final UserRepository userRepository,
                               final TokenGenerator tokenGenerator) {
    this.userRepository = userRepository;
    this.tokenGenerator = tokenGenerator;
  }

  /**
   * Authenticates a user based on their email.
   * This method checks if the user is authorized to perform certain actions
   * based on their email address.
   *
   * @param email the email of the user to be authorized; must not be {@code null}.
   * @return an {@link AuthenticationResponse} indicating the authorization status.
   */
  public AuthenticationResponse continueWithGoogle(final Email email) {
    final var user = userRepository
        .findByEmail(email)
        .orElseGet(() -> userRepository.create(email));

    final var token = tokenGenerator.generate(email);

    return new AuthenticationResponse(
        token,
        user.getId().getValue(),
        user.getRole().toString());
  }
}
