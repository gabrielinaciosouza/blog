package com.gabriel.blog.application.usecases;

import com.gabriel.blog.application.exceptions.ValidationException;
import com.gabriel.blog.application.repositories.UserRepository;
import com.gabriel.blog.application.responses.AuthenticationResponse;
import com.gabriel.blog.application.services.TokenService;
import com.gabriel.blog.domain.entities.User;
import com.gabriel.blog.domain.valueobjects.Email;
import com.gabriel.blog.domain.valueobjects.Id;
import com.gabriel.blog.domain.valueobjects.Image;
import com.gabriel.blog.domain.valueobjects.Name;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import org.jboss.logging.Logger;

/**
 * This class is responsible for handling authorization-related use cases.
 * It contains methods to check user permissions and roles.
 */
@ApplicationScoped
public final class AuthenticationUseCase {

  private static final Logger LOG = Logger.getLogger(AuthenticationUseCase.class);
  private final UserRepository userRepository;
  private final TokenService tokenService;

  /**
   * Constructs a new {@link AuthenticationUseCase} with the specified
   * {@link UserRepository} and {@link TokenService}.
   *
   * @param userRepository the repository that handles user operations; must not
   *                       be {@code null}.
   * @param tokenService   the service that handles token operations; must not be
   *                       {@code null}.
   */
  @Inject
  public AuthenticationUseCase(final UserRepository userRepository,
                               final TokenService tokenService) {
    this.userRepository = userRepository;
    this.tokenService = tokenService;
  }

  /**
   * Authenticates a user based on their Google ID token.
   *
   * @param idToken the ID token received from Google; must not be {@code null} or
   *                empty.
   * @return an {@link AuthenticationResponse} indicating the authorization status.
   */
  public AuthenticationResponse authenticateWithGoogle(final String idToken) {
    if (idToken == null || idToken.isEmpty()) {
      LOG.warn("ID token must not be null or empty");
      throw new ValidationException("ID token must not be null or empty");
    }

    final var decodedToken = tokenService.decode(idToken);

    final var email = new Email(decodedToken.email());
    final var user = userRepository.findByEmail(email)
        .orElseGet(() -> createAndSaveUser(decodedToken));

    final var token = tokenService.generate(user.getEmail());

    return new AuthenticationResponse(
        token,
        user.getId().getValue(),
        user.getRole().toString());
  }

  private User createAndSaveUser(final TokenService.DecodedToken decodedToken) {
    final var name = Optional.ofNullable(decodedToken.name())
        .map(Name::new)
        .orElseGet(() -> generateName(decodedToken.id()));
    final var image = Optional.ofNullable(decodedToken.pictureUrl())
        .map(Image::new)
        .orElse(null);
    final var newUser = new User(
        new Id(decodedToken.id()),
        new Email(decodedToken.email()),
        User.Role.USER,
        name,
        image);
    userRepository.save(newUser);
    LOG.infof("Created new user with email: %s", decodedToken.email());
    return newUser;
  }

  private Name generateName(final String id) {
    return new Name("User-" + Base64.getUrlEncoder().withoutPadding()
        .encodeToString(id.getBytes(StandardCharsets.UTF_8)));
  }
}
