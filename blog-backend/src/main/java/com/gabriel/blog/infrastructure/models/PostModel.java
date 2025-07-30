package com.gabriel.blog.infrastructure.models;

import com.gabriel.blog.domain.entities.Post;
import com.gabriel.blog.domain.valueobjects.Content;
import com.gabriel.blog.domain.valueobjects.CreationDate;
import com.gabriel.blog.domain.valueobjects.DeletedStatus;
import com.gabriel.blog.domain.valueobjects.Id;
import com.gabriel.blog.domain.valueobjects.Image;
import com.gabriel.blog.domain.valueobjects.Slug;
import com.gabriel.blog.domain.valueobjects.Title;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.errorprone.annotations.Keep;

/**
 * Represents a Firestore-compatible model for storing blog posts.
 * This class is used to convert {@link Post} entities into a format
 * that can be saved in Firestore, ensuring proper serialization.
 */
@Keep
public class PostModel {

  @DocumentId
  private String postId;
  private String title;
  private String content;
  private Timestamp creationDate;
  private String slug;
  private boolean isDeleted;
  private Timestamp deletionDate;
  private String coverImage;

  /**
   * Constructs a new {@link PostModel} instance.
   */
  public PostModel() {
  }

  /**
   * Constructs a {@link PostModel} from a given input fields.
   */
  public PostModel(final String postId, final String title, final String content,
                   final Timestamp creationDate, final String slug, final boolean isDeleted,
                   final Timestamp deletionDate, final String coverImage) {
    this.postId = postId;
    this.title = title;
    this.content = content;
    this.creationDate = creationDate;
    this.slug = slug;
    this.isDeleted = isDeleted;
    this.deletionDate = deletionDate;
    this.coverImage = coverImage;
  }

  /**
   * Constructs a {@link PostModel} from a given {@link Post} entity.
   *
   * @param post the domain entity to convert into a Firestore-compatible model.
   */
  public static PostModel from(final Post post) {
    return new PostModel(
        post.getId().getValue(),
        post.getTitle().getValue(),
        post.getContent().getValue(),
        Timestamp.ofTimeSecondsAndNanos(
            post.getCreationDate().getValue().getEpochSecond(),
            post.getCreationDate().getValue().getNano()),
        post.getSlug().getValue(),
        post.isDeleted(),
        post.isDeleted() ? Timestamp.ofTimeSecondsAndNanos(
            post.getDeletionDate().getEpochSecond(),
            post.getDeletionDate().getNano()) : null,
        post.getCoverImage().getValue().toString());
  }

  /**
   * Converts this {@link PostModel} into a domain {@link Post} entity.
   *
   * @return a {@link Post} entity representing the data in this model.
   */
  public Post toDomain() {
    return new Post(
        new Id(postId),
        new Title(title),
        new Content(content),
        new CreationDate(creationDate.toDate().toInstant()),
        Slug.fromString(slug),
        new Image(coverImage),
        new DeletedStatus(isDeleted, isDeleted ? deletionDate.toDate().toInstant() : null));
  }

  public String getPostId() {
    return postId;
  }

  public void setPostId(final String postId) {
    this.postId = postId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
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

  public String getSlug() {
    return slug;
  }

  public void setSlug(final String slug) {
    this.slug = slug;
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

  public String getCoverImage() {
    return coverImage;
  }

  public void setCoverImage(final String coverImage) {
    this.coverImage = coverImage;
  }
}