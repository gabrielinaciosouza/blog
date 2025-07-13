package com.gabriel.blog.infrastructure.repositories;

import static java.util.function.Predicate.not;

import com.gabriel.blog.application.repositories.UserRepository;
import com.gabriel.blog.domain.entities.User;
import com.gabriel.blog.domain.valueobjects.Email;
import com.gabriel.blog.domain.valueobjects.Id;
import com.gabriel.blog.domain.valueobjects.Image;
import com.gabriel.blog.domain.valueobjects.Name;
import com.gabriel.blog.infrastructure.exceptions.RepositoryException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;
import org.jboss.logging.Logger;

/**
 * This class is responsible for managing user data in Firebase Authentication.
 * It implements the {@link UserRepository} interface to provide methods for
 * finding and creating users.
 *
 * <p>Created by Gabriel Inacio de Souza on April 26, 2025.</p>
 */
@ApplicationScoped
public class FirebaseAuthUserRepository implements UserRepository {

  private static final Logger LOG = Logger.getLogger(FirebaseAuthUserRepository.class);

  final FirebaseAuth firebaseAuth;

  /**
   * Constructs a new {@link FirebaseAuthUserRepository} with the specified
   * {@link FirebaseAuth} instance.
   *
   * @param firebaseAuth the Firebase Authentication instance; must not be
   *                     {@code null}.
   */
  public FirebaseAuthUserRepository(final FirebaseAuth firebaseAuth) {
    this.firebaseAuth = firebaseAuth;

  }

  @Override
  public Optional<User> findByEmail(final Email email) {
    try {
      final var firebaseUser = firebaseAuth.getUserByEmail(email.getEmail());
      final String role = String.valueOf(firebaseUser.getCustomClaims().get("role"));
      return Optional.of(new User(
          new Id(firebaseUser.getUid()),
          email,
          Optional.ofNullable(role)
              .filter(not(String::isBlank))
              .filter(not("null"::equalsIgnoreCase))
              .map(String::toUpperCase)
              .map(User.Role::valueOf)
              .orElse(User.Role.USER),
          Optional.ofNullable(firebaseUser.getDisplayName())
              .filter(not(String::isBlank))
              .map(Name::new)
              .orElse(new Name("Unknown User")),
          Optional.ofNullable(firebaseUser.getPhotoUrl())
              .filter(not(String::isBlank))
              .map(Image::new)
              .orElse(null)));
    } catch (final FirebaseAuthException e) {
      LOG.info("User not found", e);
      return Optional.empty();
    } catch (final Exception e) {
      LOG.error("Failed to find user by email", e);
      throw new RepositoryException("Failed to find user by email", e);
    }
  }

  @Override
  public void save(final User user) {
    try {
      final var firebaseUser = firebaseAuth.createUser(
          new UserRecord.CreateRequest()
              .setEmail(user.getEmail().getEmail())
              .setDisplayName(user.getName().getValue())
              .setUid(user.getId().getValue())
              .setPhotoUrl(user.getPictureUrl().getValue().toString()));

      firebaseUser.getCustomClaims().put("role", user.getRole().name());
      firebaseAuth.setCustomUserClaims(firebaseUser.getUid(), firebaseUser.getCustomClaims());
    } catch (final Exception e) {
      LOG.error("Failed to create user", e);
      throw new RepositoryException("Failed to create user", e);
    }
  }
}
