package com.gabriel.blog.application.responses;

import com.gabriel.blog.domain.entities.Post;

/**
 * Represents the response returned after creating a new {@link Post}.
 * This DTO (Data Transfer Object) is used to convey the data of the created post
 * back to the client in a structured format.
 *
 * <p>Created by Gabriel Inacio de Souza on February 2, 2025.</p>
 *
 * <p>This class provides a simple representation of the {@link Post} entity
 * containing the post ID, title, content, and creation date, making it suitable for
 * returning as a response to a client after successfully creating a post.</p>
 */
public class CreatePostResponse {

  private String postId;
  private String title;
  private String content;
  private String creationDate;

  public CreatePostResponse(final String postId, final String title, final String content,
                            final String creationDate) {
    this.postId = postId;
    this.title = title;
    this.content = content;
    this.creationDate = creationDate;
  }

  public CreatePostResponse() {
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

