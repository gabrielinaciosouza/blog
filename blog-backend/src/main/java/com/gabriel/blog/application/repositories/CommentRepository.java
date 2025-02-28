package com.gabriel.blog.application.repositories;

import com.gabriel.blog.domain.entities.Comment;

/**
 * Repository for the {@code Comment} entity.
 */
public interface CommentRepository {

  /**
   * Saves a {@link Comment} entity.
   * This method is responsible for persisting a {@link Comment} entity in the database.
   *
   * <p>The implementation should save the comment in the database.</p>
   *
   * @param comment the comment to save.
   */
  void save(Comment comment);
}
