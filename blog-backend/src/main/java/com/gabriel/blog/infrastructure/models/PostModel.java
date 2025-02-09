package com.gabriel.blog.infrastructure.models;

import com.gabriel.blog.domain.entities.Post;
import com.gabriel.blog.domain.valueobjects.Content;
import com.gabriel.blog.domain.valueobjects.CreationDate;
import com.gabriel.blog.domain.valueobjects.Id;
import com.gabriel.blog.domain.valueobjects.Slug;
import com.gabriel.blog.domain.valueobjects.Title;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import java.time.LocalDate;

/**
 * Represents a Firestore-compatible model for storing blog posts.
 * This class is used to convert {@link Post} entities into a format
 * that can be saved in Firestore, ensuring proper serialization.
 */
public class PostModel {

  private String title;
  private String content;
  private String creationDate;
  private String slug;

  /**
   * Constructs a new {@link PostModel} instance.
   */
  public PostModel() {
  }

  /**
   * Constructs a {@link PostModel} from a given input fields.
   */
  public PostModel(final String title, final String content,
                   final String creationDate, final String slug) {
    this.title = title;
    this.content = content;
    this.creationDate = creationDate;
    this.slug = slug;
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
        post.getCreationDate().toString(),
        post.getSlug().getValue());
  }

  /**
   * Converts a Firestore document snapshot into a {@link Post} domain entity.
   *
   * @param document the Firestore document to convert into a domain entity.
   * @return a new {@link Post} instance.
   */
  public static Post toDomain(final QueryDocumentSnapshot document) {
    final var doc = document.toObject(PostModel.class);
    return new Post(
        new Id(document.getId()),
        new Title(doc.getTitle()),
        new Content(doc.getContent()),
        new CreationDate(LocalDate.parse(doc.getCreationDate())),
        Slug.fromString(doc.getSlug())
    );
  }

  public String getTitle() {
    return title;
  }

  public String getContent() {
    return content;
  }

  public String getCreationDate() {
    return creationDate;
  }

  public String getSlug() {
    return slug;
  }
}
