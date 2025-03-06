package com.gabriel.blog.application.repositories;

import com.gabriel.blog.domain.entities.Author;
import com.gabriel.blog.domain.valueobjects.Id;
import java.util.Optional;

/**
 * Repository for the {@link Author} entity.
 */
public interface AuthorRepository {

  /**
   * Returns whether an author exists by the given ID.
   *
   * @param id the author ID.
   * @return Optional of {@code Author} if the author exists, empty otherwise.
   */
  Optional<Author> findById(Id id);
}
