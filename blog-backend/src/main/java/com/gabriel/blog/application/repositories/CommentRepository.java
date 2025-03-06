package com.gabriel.blog.application.repositories;

import com.gabriel.blog.domain.entities.Comment;
import com.gabriel.blog.domain.valueobjects.Id;
import java.util.List;

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

  /**
   * Gets a list of comments by their IDs.
   *
   * @param ids the list of IDs to get.
   * @return the list of comments.
   */
  List<Comment> getCommentsById(List<Id> ids);
}
