package com.gabriel.blog.infrastructure.models;

import com.gabriel.blog.domain.entities.Post;

/**
 * Represents a Firestore-compatible model for storing blog posts.
 * This class is used to convert {@link Post} entities into a format
 * that can be saved in Firestore, ensuring proper serialization.
 */
public class PostModel {

  private String title;
  private String content;
  private String creationDate;

  /**
   * Constructs a {@link PostModel} from a given input fields.
   */
  public PostModel(final String title, final String content,
                   final String creationDate) {
    this.title = title;
    this.content = content;
    this.creationDate = creationDate;
  }

  /**
   * Constructs a {@link PostModel} from a given {@link Post} entity.
   *
   * @param post the domain entity to convert into a Firestore-compatible model.
   */
  public static PostModel from(final Post post) {
    return new PostModel(
        post.getTitle().getValue(),
        post.getContent().getValue(),
        post.getCreationDate().toString());
  }

  public String getTitle() {
    return title;
  }

  public String getContent() {
    return content;
  }

}
