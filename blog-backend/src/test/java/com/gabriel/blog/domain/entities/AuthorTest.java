package com.gabriel.blog.domain.entities;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gabriel.blog.domain.exceptions.DomainException;
import com.gabriel.blog.domain.valueobjects.CreationDate;
import com.gabriel.blog.domain.valueobjects.Email;
import com.gabriel.blog.domain.valueobjects.Id;
import com.gabriel.blog.domain.valueobjects.Image;
import com.gabriel.blog.domain.valueobjects.Name;
import com.gabriel.blog.fixtures.CreationDateFixture;
import com.gabriel.blog.fixtures.EmailFixture;
import com.gabriel.blog.fixtures.IdFixture;
import com.gabriel.blog.fixtures.ImageFixture;
import com.gabriel.blog.fixtures.NameFixture;
import org.junit.jupiter.api.Test;

class AuthorTest {

  private static final Id id = IdFixture.withId("any");
  private static final Name name = NameFixture.name();
  private static final Email email = EmailFixture.email();
  private static final CreationDate creationDate = CreationDateFixture.creationDate();
  private static final Image profilePicture = ImageFixture.image();

  @Test
  void shouldCreateCorrectAuthor() {
    final var nullIdException =
        assertThrows(DomainException.class,
            () -> new Author(null, name, email, creationDate, profilePicture));
    assertEquals("Tried to create an Entity with a null id", nullIdException.getMessage());

    final var nullNameException =
        assertThrows(DomainException.class,
            () -> new Author(id, null, email, creationDate, profilePicture));
    assertEquals("Tried to create a User with a null name", nullNameException.getMessage());

    final var nullEmailException =
        assertThrows(DomainException.class,
            () -> new Author(id, name, null, creationDate, profilePicture));
    assertEquals("Tried to create a User with a null email", nullEmailException.getMessage());

    final var nullCreationDateException =
        assertThrows(DomainException.class,
            () -> new Author(id, name, email, null, profilePicture));
    assertEquals("Tried to create a User with a null creationDate",
        nullCreationDateException.getMessage());

    final var nullProfilePictureException =
        assertThrows(DomainException.class,
            () -> new Author(id, name, email, creationDate, null));
    assertEquals("Tried to create a User with a null profilePicture",
        nullProfilePictureException.getMessage());

    assertDoesNotThrow(() -> new Author(id, name, email, creationDate, profilePicture));
  }

  @Test
  void shouldCreateCorrectToString() {
    final var user = new Author(id, name, email, creationDate, profilePicture);
    assertEquals("Author {"
        + "\"creationDate\":\"2024-12-12 01:00\","
        + "\"email\":\"Email {\\\"email\\\":\\\"email@email.com\\\"}\","
        + "\"name\":\"Name {\\\"value\\\":\\\"any name\\\"}\","
        + "\"profilePicture\":\"https:\\/\\/example.com\\/image.jpg\","
        + "\"id\":\"Id {\\\"value\\\":\\\"any\\\"}\"}", user.toString());
  }

}