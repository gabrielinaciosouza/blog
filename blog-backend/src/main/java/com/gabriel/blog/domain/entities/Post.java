package com.gabriel.blog.domain.entities;

import com.gabriel.blog.domain.abstractions.AbstractEntity;
import com.gabriel.blog.domain.exceptions.DomainException;
import com.gabriel.blog.domain.valueobjects.Content;
import com.gabriel.blog.domain.valueobjects.CreationDate;
import com.gabriel.blog.domain.valueobjects.DeletedStatus;
import com.gabriel.blog.domain.valueobjects.Id;
import com.gabriel.blog.domain.valueobjects.Image;
import com.gabriel.blog.domain.valueobjects.Slug;
import com.gabriel.blog.domain.valueobjects.Title;
import java.time.Instant;

/**
 * Represents a blog post as an entity.
 * This class ensures that the title, content, creation date, and slug are not null.
 * It also provides methods to mark the post as deleted or not deleted.
 *
 * <p>Created by Gabriel Inacio de Souza on February 1, 2025.</p>
 */
public class Post extends AbstractEntity {

  private static final Image DEFAULT_IMAGE = new Image(
      "https://media.licdn.com/dms/image/v2/D4D03AQFYx_k72IBkLg/profile-displayphoto-shrink_800_800/B4DZRGBk.9HYAc-/0/1736341607395?e=1745452800&v=beta&t=lWzoy4ZaGd3a4LDrxBNEA0_sderdTDQKL41lVFvtWug");

  private final Title title;
  private final Content content;
  private final CreationDate creationDate;
  private final Slug slug;
  private final Image coverImage;
  private DeletedStatus deletedStatus;

  /**
   * Constructs a {@code Post} instance with the given
   * title, content, creation date, slug, cover image, and deleted status.
   *
   * @param id            the post id, must not be null
   * @param title         the post title, must not be null
   * @param content       the post content, must not be null
   * @param creationDate  the post creation date, must not be null
   * @param slug          the post slug, must not be null
   * @param coverImage    the post cover image, must not be null
   * @param deletedStatus the post deleted status, must not be null
   * @throws DomainException if any of the provided parameters is null
   */
  public Post(final Id id, final Title title, final Content content,
              final CreationDate creationDate, final Slug slug, final Image coverImage,
              final DeletedStatus deletedStatus) {
    super(id);
    this.title = nonNull(title, "Tried to create a Post with a null title");
    this.content = nonNull(content, "Tried to create a Post with a null content");
    this.creationDate = nonNull(creationDate, "Tried to create a Post with a null creationDate");
    this.slug = nonNull(slug, "Tried to create a Post with a null slug");
    this.coverImage = coverImage == null ? DEFAULT_IMAGE : coverImage;
    this.deletedStatus = nonNull(deletedStatus, "Tried to create a Post with a null deletedStatus");
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

  public Image getCoverImage() {
    return coverImage;
  }

  public boolean isDeleted() {
    return deletedStatus.isDeleted();
  }

  /**
   * Marks the post as deleted.
   */
  public void markAsDeleted() {
    this.deletedStatus = DeletedStatus.deleted();
  }

  /**
   * Marks the post as not deleted.
   */
  public void markAsNotDeleted() {
    this.deletedStatus = DeletedStatus.notDeleted();
  }

  public Instant getDeletionDate() {
    return deletedStatus.getDeletionDate();
  }
}