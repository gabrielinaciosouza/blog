package com.gabriel.blog.domain.entities;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gabriel.blog.domain.exceptions.DomainException;
import com.gabriel.blog.domain.valueobjects.Content;
import com.gabriel.blog.domain.valueobjects.CreationDate;
import com.gabriel.blog.domain.valueobjects.Id;
import com.gabriel.blog.domain.valueobjects.Slug;
import com.gabriel.blog.domain.valueobjects.Title;
import com.gabriel.blog.fixtures.ContentFixture;
import com.gabriel.blog.fixtures.CreationDateFixture;
import com.gabriel.blog.fixtures.IdFixture;
import com.gabriel.blog.fixtures.SlugFixture;
import com.gabriel.blog.fixtures.TitleFixture;
import org.junit.jupiter.api.Test;

class PostTest {

  private static final Id id = IdFixture.withId("any");
  private static final Title title = TitleFixture.title();
  private static final Content content = ContentFixture.content();
  private static final CreationDate creationDate = CreationDateFixture.creationDate();
  private static final Slug slug = SlugFixture.slug();

  @Test
  void shouldCreateCorrectPost() {
    final var nullIdException =
        assertThrows(DomainException.class,
            () -> new Post(null, title, content, creationDate, slug));
    assertEquals("Tried to create an Entity with a null id", nullIdException.getMessage());

    final var nullTitleException =
        assertThrows(DomainException.class, () -> new Post(id, null, content, creationDate, slug));
    assertEquals("Tried to create a Post with a null title", nullTitleException.getMessage());

    final var nullContentException =
        assertThrows(DomainException.class, () -> new Post(id, title, null, creationDate, slug));
    assertEquals("Tried to create a Post with a null content", nullContentException.getMessage());

    final var nullCreationDateException =
        assertThrows(DomainException.class, () -> new Post(id, title, content, null, slug));
    assertEquals("Tried to create a Post with a null creationDate",
        nullCreationDateException.getMessage());

    final var nullSlugException =
        assertThrows(DomainException.class, () -> new Post(id, title, content, creationDate, null));
    assertEquals("Tried to create a Post with a null slug",
        nullSlugException.getMessage());

    assertDoesNotThrow(() -> new Post(id, title, content, creationDate, slug));
  }

  @Test
  void shouldCreateCorrectToString() {
    final var post = new Post(id, title, content, creationDate, slug);
    assertEquals(
        "Post {\"content\":\"Content {\\\"value\\\":\\\"any content\\\"}\","
            + "\"creationDate\":\"2024-12-12 01:00\","
            + "\"slug\":\"Slug {\\\"value\\\":\\\"any-title\\\"}\","
            + "\"title\":\"Title {\\\"value\\\":\\\"any title\\\"}\","
            + "\"id\":\"Id {\\\"value\\\":\\\"any\\\"}\"}",
        post.toString());
  }
}
