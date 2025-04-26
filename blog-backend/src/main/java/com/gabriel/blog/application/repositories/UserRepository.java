package com.gabriel.blog.application.repositories;

import com.gabriel.blog.domain.entities.User;
import com.gabriel.blog.domain.valueobjects.Email;
import java.util.Optional;

/**
 * The {@link UserRepository} interface defines the contract for user-related operations.
 * It serves as a marker interface for repositories that handle user data.
 *
 * <p>Created by Gabriel Inacio de Souza on April 26, 2025.</p>
 *
 * <p>This interface is part of the application's repository layer,
 * which is responsible for abstracting the data access logic
 * and providing a clean API for interacting with user data.</p>
 */
public interface UserRepository {

  /**
   * Finds a user by their email address.
   *
   * @param email the email address of the user to find; must not be {@code null}.
   * @return an {@link Optional} containing {@link User} if present, or empty if not found.
   */
  Optional<User> findByEmail(Email email);

  /**
   * Saves a new user to the repository.
   *
   * @param user the {@link User} to save; must not be {@code null}.
   * @return the saved {@link User}.
   */
  User create(Email user);
}
