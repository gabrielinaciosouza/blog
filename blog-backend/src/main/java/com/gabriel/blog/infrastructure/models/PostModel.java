package com.gabriel.blog.infrastructure.models;

import com.gabriel.blog.domain.entities.Post;

/**
 * Represents a Firestore-compatible model for storing blog posts.
 * This class is used to convert {@link Post} entities into a format
 * that can be saved in Firestore, ensuring proper serialization.
 */
public class PostModel {

  private String postId;
  private String title;
  private String content;
  private String creationDate;

  /**
   * Default constructor required by Firestore for deserialization.
   */
  public PostModel() {
  }

  /**
   * Constructs a {@link PostModel} from a given {@link Post} entity.
   *
   * @param post the domain entity to convert into a Firestore-compatible model.
   */
  public PostModel(final Post post) {
    this.postId = post.getId().getValue();
    this.title = post.getTitle().getValue();
    this.content = post.getContent().getValue();
    this.creationDate = post.getCreationDate().toString();
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

  public String getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(final String creationDate) {
    this.creationDate = creationDate;
  }
}
