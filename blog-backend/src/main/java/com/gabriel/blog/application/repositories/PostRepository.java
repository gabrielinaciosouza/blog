package com.gabriel.blog.application.repositories;

import com.gabriel.blog.domain.entities.Post;
import com.gabriel.blog.domain.valueobjects.Slug;
import java.util.Optional;

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

  /**
   * Retrieves a {@link Post} entity by its unique slug.
   * This method is responsible for fetching a {@link Post} entity from the database
   * based on its unique slug value.
   *
   * <p>The implementation should query the database for a post with the given slug,
   * and return an {@link Optional} containing the post if it exists, or an empty
   * {@link Optional} if no post with the given slug was found.</p>
   *
   * @param slug the unique slug value of the post to retrieve.
   * @return an {@link Optional} with the post, or an empty {@link Optional}.
   */
  Optional<Post> findBySlug(Slug slug);
}