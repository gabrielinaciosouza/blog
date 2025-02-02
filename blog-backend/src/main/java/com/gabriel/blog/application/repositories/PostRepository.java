package com.gabriel.blog.application.repositories;

import com.gabriel.blog.domain.entities.Post;

/**
 * Interface for repository operations related to {@link Post} entities.
 * This interface defines the contract for saving {@link Post} entities in the persistence layer.
 *
 * <p>Created by Gabriel Inacio de Souza on February 2, 2025.</p>
 *
 * <p>Implementations of this interface will provide the actual logic for saving {@link Post}
 * entities to the database, whether it's using an ORM framework like Hibernate,
 * or a NoSQL database like Firestore.</p>
 */
public interface PostRepository {

  /**
   * Saves the provided {@link Post} entity to the database.
   * This method is responsible for persisting the state of the {@link Post}
   * entity to the underlying storage system.
   *
   * <p>The implementation should handle any necessary database-specific logic, such as
   * generating unique identifiers, handling transactions, or ensuring consistency.</p>
   *
   * @param post the {@link Post} entity to be saved; must not be {@code null}.
   * @throws IllegalArgumentException if the {@code post} is {@code null}.
   */
  void save(Post post);
}