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
  private static final Id postId = IdFixture.postId();
  private static final DeletedStatus deletedStatus = DeletedStatus.notDeleted();

  @Test
  void shouldCreateCorrectComment() {
    final var nullIdException =
        assertThrows(DomainException.class,
            () -> new Comment(null, authorId, content, creationDate, postId, deletedStatus));
    assertEquals("Tried to create an Entity with a null id", nullIdException.getMessage());

    final var nullAuthorNameException =
        assertThrows(DomainException.class,
            () -> new Comment(IdFixture.withId("any"), null, content, creationDate, postId,
                deletedStatus));
    assertEquals("Tried to create a Comment with a null authorId",
        nullAuthorNameException.getMessage());

    final var nullContentException =
        assertThrows(DomainException.class,
            () -> new Comment(IdFixture.withId("any"), authorId, null, creationDate, postId,
                deletedStatus));
    assertEquals("Tried to create a Comment with a null content",
        nullContentException.getMessage());

    final var nullCreationDateException =
        assertThrows(DomainException.class,
            () -> new Comment(IdFixture.withId("any"), authorId, content, null, postId,
                deletedStatus));
    assertEquals("Tried to create a Comment with a null creationDate",
        nullCreationDateException.getMessage());

    final var nullPostIdException =
        assertThrows(DomainException.class,
            () -> new Comment(IdFixture.withId("any"), authorId, content, creationDate, null,
                deletedStatus));
    assertEquals("Tried to create a Comment with a null postId", nullPostIdException.getMessage());

    final var nullDeletedStatusException =
        assertThrows(DomainException.class,
            () -> new Comment(IdFixture.withId("any"), authorId, content, creationDate, postId,
                null));
    assertEquals("Tried to create a Comment with a null deletedStatus",
        nullDeletedStatusException.getMessage());

    assertDoesNotThrow(
        () -> new Comment(IdFixture.withId("any"), authorId, content, creationDate, postId,
            deletedStatus));
  }

  @Test
  void shouldCreateCorrectToString() {
    final var comment =
        new Comment(IdFixture.withId("any"), authorId, content, creationDate, postId,
            deletedStatus);
    assertEquals(
        "Comment {"
            + "\"authorId\":\"Id {\\\"value\\\":\\\"author id\\\"}\","
            + "\"content\":\"Content {\\\"value\\\":\\\"any content\\\"}\","
            + "\"creationDate\":\"2024-12-12 01:00\","
            + "\"deletedStatus\":\"DeletedStatus {\\\"deletionDate\\\":null,\\\"value\\\":false}\","
            + "\"postId\":\"Id {\\\"value\\\":\\\"post id\\\"}\""
            + ",\"id\":\"Id {\\\"value\\\":\\\"any\\\"}\"}",
        comment.toString());
  }
}