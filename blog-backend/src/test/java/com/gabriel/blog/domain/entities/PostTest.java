package com.gabriel.blog.domain.entities;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gabriel.blog.domain.exceptions.DomainException;
import com.gabriel.blog.domain.valueobjects.Content;
import com.gabriel.blog.domain.valueobjects.CreationDate;
import com.gabriel.blog.domain.valueobjects.Id;
import com.gabriel.blog.domain.valueobjects.Title;
import com.gabriel.blog.fixtures.IdFixture;
import org.junit.jupiter.api.Test;

class PostTest {

  private static final Id id = IdFixture.withId("any");
  private static final Title title = new Title("any title");
  private static final Content content = new Content("any content");
  private static final CreationDate creationDate = CreationDate.now();

  @Test
  void shouldCreateCorrectPost() {
    final var nullIdException =
        assertThrows(DomainException.class, () -> new Post(null, title, content, creationDate));
    assertEquals("Tried to create an Entity with a null id", nullIdException.getMessage());

    final var nullTitleException =
        assertThrows(DomainException.class, () -> new Post(id, null, content, creationDate));
    assertEquals("Tried to create a Post with a null title", nullTitleException.getMessage());

    final var nullContentException =
        assertThrows(DomainException.class, () -> new Post(id, title, null, creationDate));
    assertEquals("Tried to create a Post with a null content", nullContentException.getMessage());

    final var nullCreationDateException =
        assertThrows(DomainException.class, () -> new Post(id, title, content, null));
    assertEquals("Tried to create a Post with a null creationDate",
        nullCreationDateException.getMessage());

    assertDoesNotThrow(() -> new Post(id, title, content, creationDate));
  }

  @Test
  void shouldCreateCorrectToString() {
    final var post = new Post(id, title, content, creationDate);
    assertEquals(
        "Post {\"content\":\"Content {\\\"value\\\":\\\"any content\\\"}\","
            + "\"creationDate\":\"" + creationDate
            + "\",\"title\":\"Title {\\\"value\\\":\\\"any title\\\"}\","
            + "\"id\":\"Id {\\\"value\\\":\\\"any\\\"}\"}",
        post.toString());
  }
}
