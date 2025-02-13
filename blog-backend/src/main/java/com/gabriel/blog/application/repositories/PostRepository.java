package com.gabriel.blog.application.repositories;

import com.gabriel.blog.domain.entities.Post;
import com.gabriel.blog.domain.valueobjects.Slug;
import java.util.List;
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

  /**
   * Retrieves a list of {@link Post} entities based on the provided search criteria.
   * This method is responsible for fetching a list of {@link Post} entities from the database
   * based on the provided search parameters.
   *
   * <p>The implementation should query the database for posts that match the search criteria,
   * and return a list of posts that satisfy the conditions specified in the search parameters.</p>
   *
   * @param params the search parameters to filter the list of posts.
   * @return a list of {@link Post} entities that match the search criteria.
   */
  List<Post> findPosts(FindPostsParams params);

  /**
   * Represents the sorting criteria used to order the list of posts.
   * This enum defines the possible sorting criteria that can be used to order the list of posts.
   *
   * <p>Created by Gabriel Inacio de Souza on February 2, 2025.</p>
   *
   * <p>The sorting criteria can be either by title or by date, and is used to specify
   * the field by which the posts should be sorted.</p>
   */
  enum SortBy {
    title, creationDate
  }

  /**
   * Represents the sorting order used to order the list of posts.
   * This enum defines the possible sorting orders that can be used to order the list of posts.
   *
   * <p>Created by Gabriel Inacio de Souza on February 2, 2025.</p>
   *
   * <p>The sorting order can be either ascending (ASC) or descending (DESC),
   * and is used to specify the order in which the posts should be sorted.</p>
   */
  enum SortOrder {
    ASCENDING, DESCENDING
  }

  /**
   * Represents the search parameters used to filter the list of posts.
   * This record contains the search criteria used to filter the list of posts
   * based on the specified page, size, sorting order, and title.
   *
   * <p>Created by Gabriel Inacio de Souza on February 2, 2025.</p>
   *
   * <p>This record provides a structured representation of the search parameters
   * used to filter the list of posts, making it easier to pass the search criteria
   * to the repository method that retrieves the list of posts.</p>
   */
  record FindPostsParams(int page, int size, SortBy sortBy, SortOrder sortOrder) {
  }
}