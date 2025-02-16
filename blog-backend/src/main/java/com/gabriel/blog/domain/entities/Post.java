package com.gabriel.blog.domain.entities;

import com.gabriel.blog.domain.abstractions.AbstractEntity;
import com.gabriel.blog.domain.exceptions.DomainException;
import com.gabriel.blog.domain.valueobjects.Content;
import com.gabriel.blog.domain.valueobjects.CreationDate;
import com.gabriel.blog.domain.valueobjects.Id;
import com.gabriel.blog.domain.valueobjects.Slug;
import com.gabriel.blog.domain.valueobjects.Title;


/**
 * Represents a blog post in the system.
 * A post consists of a title, content, slug, and creation date.
 *
 * <p>Created by Gabriel Inacio de Souza on February 1, 2025.</p>
 */
public class Post extends AbstractEntity {

  private final Title title;
  private final Content content;
  private final CreationDate creationDate;
  private final Slug slug;

  /**
   * Constructs a {@code Post} object with the specified values.
   *
   * @param id           the identifier of the post
   * @param title        the title of the post
   * @param content      the content of the post
   * @param creationDate the creation date of the post
   * @param slug the creation date of the post
   * @throws DomainException if any of the arguments are {@code null}
   */
  public Post(
      final Id id,
      final Title title,
      final Content content,
      final CreationDate creationDate,
      final Slug slug) {
    super(id);
    this.title = nonNull(title, "Tried to create a Post with a null title");
    this.content = nonNull(content, "Tried to create a Post with a null content");
    this.creationDate = nonNull(creationDate, "Tried to create a Post with a null creationDate");
    this.slug = nonNull(slug, "Tried to create a Post with a null slug");
  }

  public Title getTitle() {
    return title;
  }

  public Content getContent() {
    return content;
  }

  public CreationDate getCreationDate() {
    return creationDate;
  }

  public Slug getSlug() {
    return slug;
  }
}
