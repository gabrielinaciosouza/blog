package com.gabriel.blog.domain.entities;

import com.gabriel.blog.domain.abstractions.AbstractEntity;
import com.gabriel.blog.domain.valueobjects.Content;
import com.gabriel.blog.domain.valueobjects.CreationDate;
import com.gabriel.blog.domain.valueobjects.DeletedStatus;
import com.gabriel.blog.domain.valueobjects.Id;
import java.time.Instant;

/**
 * Represents a comment as an entity.
 * This class ensures that the author name, content, creation date, post id, and deleted status are not null.
 *
 * <p>Created by Gabriel Inacio de Souza on February 27, 2025.</p>
 */
public class Comment extends AbstractEntity {

  private final Id authorId;
  private final Content content;
  private final CreationDate creationDate;
  private final DeletedStatus deletedStatus;

  /**
   * Constructs a {@code Comment} instance with the given
   * author name, content, creation date, post id, and deleted status.
   *
   * @param id            the comment id, must not be null
   * @param authorId      the comment author id, must not be null
   * @param content       the comment content, must not be null
   * @param creationDate  the comment creation date, must not be null
   * @param deletedStatus the comment deleted status, must not be null
   */
  public Comment(
      final Id id,
      final Id authorId,
      final Content content,
      final CreationDate creationDate,
      final DeletedStatus deletedStatus) {
    super(id);
    this.authorId = nonNull(authorId, "Tried to create a Comment with a null authorId");
    this.content = nonNull(content, "Tried to create a Comment with a null content");
    this.creationDate = nonNull(creationDate, "Tried to create a Comment with a null creationDate");
    this.deletedStatus =
        nonNull(deletedStatus, "Tried to create a Comment with a null deletedStatus");
  }

  public Id getAuthorId() {
    return authorId;
  }

  public Content getContent() {
    return content;
  }

  public CreationDate getCreationDate() {
    return creationDate;
  }

  public DeletedStatus getDeletedStatus() {
    return deletedStatus;
  }

  public boolean isDeleted() {
    return deletedStatus.isDeleted();
  }

  public Instant getDeletionDate() {
    return deletedStatus.getDeletionDate();
  }
}
