package com.gabriel.blog.infrastructure.models;

import com.gabriel.blog.domain.entities.Comment;
import com.gabriel.blog.domain.valueobjects.Content;
import com.gabriel.blog.domain.valueobjects.CreationDate;
import com.gabriel.blog.domain.valueobjects.DeletedStatus;
import com.gabriel.blog.domain.valueobjects.Id;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;

/**
 * Represents a Firestore-compatible model for storing blog comments.
 * This class is used to convert {@link CommentModel} entities into a format
 * that can be saved in Firestore, ensuring proper serialization.
 */
public class CommentModel {

  @DocumentId
  private String commentId;
  private String authorId;
  private String content;
  private Timestamp creationDate;
  private boolean isDeleted;
  private Timestamp deletionDate;

  /**
   * Constructs a new {@link CommentModel} instance.
   */
  public CommentModel() {

  }

  /**
   * Constructs a {@link CommentModel} from a given input fields.
   */
  public CommentModel(final String commentId,
                      final String authorId,
                      final String content,
                      final Timestamp creationDate,
                      final boolean isDeleted,
                      final Timestamp deletionDate) {
    this.commentId = commentId;
    this.authorId = authorId;
    this.content = content;
    this.creationDate = creationDate;
    this.isDeleted = isDeleted;
    this.deletionDate = deletionDate;
  }

  /**
   * Converts a given {@link Comment} domain entity into a {@link CommentModel}.
   *
   * @param comment the {@link Comment} domain entity to convert.
   * @return the corresponding {@link CommentModel}.
   */
  public static CommentModel from(final Comment comment) {
    return new CommentModel(
        comment.getId().getValue(),
        comment.getAuthorId().getValue(),
        comment.getContent().getValue(),
        Timestamp.ofTimeSecondsAndNanos(
            comment.getCreationDate().getValue().getEpochSecond(),
            comment.getCreationDate().getValue().getNano()),
        comment.isDeleted(),
        comment.getDeletedStatus().isDeleted() ? Timestamp.ofTimeSecondsAndNanos(
            comment.getDeletionDate().getEpochSecond(),
            comment.getDeletionDate().getNano()) : null);
  }

  /**
   * Converts this {@link CommentModel} into a {@link Comment} domain entity.
   *
   * @return the corresponding {@link Comment} domain entity.
   */
  public Comment toDomain() {
    return new Comment(
        new Id(commentId),
        new Id(authorId),
        new Content(content),
        new CreationDate(creationDate.toDate().toInstant()),
        new DeletedStatus(isDeleted, isDeleted ? deletionDate.toDate().toInstant() : null));
  }

  public String getCommentId() {
    return commentId;
  }

  public void setCommentId(final String commentId) {
    this.commentId = commentId;
  }

  public String getAuthorId() {
    return authorId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(final String content) {
    this.content = content;
  }

  public Timestamp getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(final Timestamp creationDate) {
    this.creationDate = creationDate;
  }

  public boolean isDeleted() {
    return isDeleted;
  }

  public void setDeleted(final boolean deleted) {
    isDeleted = deleted;
  }

  public Timestamp getDeletionDate() {
    return deletionDate;
  }

  public void setDeletionDate(final Timestamp deletionDate) {
    this.deletionDate = deletionDate;
  }
}
