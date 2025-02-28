package com.gabriel.blog.application.repositories;

import com.gabriel.blog.domain.valueobjects.Id;

/**
 * Repository for the {@code User} entity.
 */
public interface UserRepository {

  /**
   * Checks if a user exists by the given id.
   *
   * @param id the id to check
   * @return {@code true} if the user exists, {@code false} otherwise
   */
  boolean existsById(Id id);
}
