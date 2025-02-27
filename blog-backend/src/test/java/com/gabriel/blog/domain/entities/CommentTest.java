package com.gabriel.blog.domain.entities;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gabriel.blog.domain.exceptions.DomainException;
import com.gabriel.blog.domain.valueobjects.Content;
import com.gabriel.blog.domain.valueobjects.CreationDate;
import com.gabriel.blog.domain.valueobjects.DeletedStatus;
import com.gabriel.blog.domain.valueobjects.Id;
import com.gabriel.blog.fixtures.ContentFixture;
import com.gabriel.blog.fixtures.CreationDateFixture;
import com.gabriel.blog.fixtures.IdFixture;
import org.junit.jupiter.api.Test;

class CommentTest {

  private static final Id authorId = IdFixture.authorId();
  private static final Content content = ContentFixture.content();
  private static final CreationDate creationDate = CreationDateFixture.creationDate();
  private static final DeletedStatus deletedStatus = DeletedStatus.notDeleted();

  @Test
  void shouldCreateCorrectComment() {
    final var nullIdException =
        assertThrows(DomainException.class,
            () -> new Comment(null, authorId, content, creationDate, deletedStatus));
    assertEquals("Tried to create an Entity with a null id", nullIdException.getMessage());

    final var nullAuthorNameException =
        assertThrows(DomainException.class,
            () -> new Comment(IdFixture.withId("any"), null, content, creationDate,
                deletedStatus));
    assertEquals("Tried to create a Comment with a null authorId",
        nullAuthorNameException.getMessage());

    final var nullContentException =
        assertThrows(DomainException.class,
            () -> new Comment(IdFixture.withId("any"), authorId, null, creationDate,
                deletedStatus));
    assertEquals("Tried to create a Comment with a null content",
        nullContentException.getMessage());

    final var nullCreationDateException =
        assertThrows(DomainException.class,
            () -> new Comment(IdFixture.withId("any"), authorId, content, null,
                deletedStatus));
    assertEquals("Tried to create a Comment with a null creationDate",
        nullCreationDateException.getMessage());

    final var nullDeletedStatusException =
        assertThrows(DomainException.class,
            () -> new Comment(IdFixture.withId("any"), authorId, content, creationDate,
                null));
    assertEquals("Tried to create a Comment with a null deletedStatus",
        nullDeletedStatusException.getMessage());

    assertDoesNotThrow(
        () -> new Comment(IdFixture.withId("any"), authorId, content, creationDate,
            deletedStatus));
  }

  @Test
  void shouldCreateCorrectToString() {
    final var comment =
        new Comment(IdFixture.withId("any"), authorId, content, creationDate,
            deletedStatus);
    assertEquals(
        "Comment {"
            + "\"authorId\":\"Id {\\\"value\\\":\\\"author id\\\"}\","
            + "\"content\":\"Content {\\\"value\\\":\\\"any content\\\"}\","
            + "\"creationDate\":\"2024-12-12 01:00\","
            + "\"deletedStatus\":\"DeletedStatus {\\\"deletionDate\\\":null,\\\"value\\\":false}\","
            + "\"id\":\"Id {\\\"value\\\":\\\"any\\\"}\"}",
        comment.toString());
  }
}