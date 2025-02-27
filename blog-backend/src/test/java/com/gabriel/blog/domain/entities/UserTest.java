package com.gabriel.blog.domain.entities;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gabriel.blog.domain.exceptions.DomainException;
import com.gabriel.blog.domain.valueobjects.CreationDate;
import com.gabriel.blog.domain.valueobjects.Email;
import com.gabriel.blog.domain.valueobjects.Id;
import com.gabriel.blog.domain.valueobjects.Name;
import com.gabriel.blog.fixtures.CreationDateFixture;
import com.gabriel.blog.fixtures.EmailFixture;
import com.gabriel.blog.fixtures.IdFixture;
import com.gabriel.blog.fixtures.NameFixture;
import org.junit.jupiter.api.Test;

class UserTest {

  private static final Id id = IdFixture.withId("any");
  private static final Name name = NameFixture.name();
  private static final Email email = EmailFixture.email();
  private static final CreationDate creationDate = CreationDateFixture.creationDate();

  @Test
  void shouldCreateCorrectUser() {
    final var nullIdException =
        assertThrows(DomainException.class,
            () -> new User(null, name, email, creationDate));
    assertEquals("Tried to create an Entity with a null id", nullIdException.getMessage());

    final var nullNameException =
        assertThrows(DomainException.class,
            () -> new User(id, null, email, creationDate));
    assertEquals("Tried to create a User with a null name", nullNameException.getMessage());

    final var nullEmailException =
        assertThrows(DomainException.class,
            () -> new User(id, name, null, creationDate));
    assertEquals("Tried to create a User with a null email", nullEmailException.getMessage());

    final var nullCreationDateException =
        assertThrows(DomainException.class,
            () -> new User(id, name, email, null));
    assertEquals("Tried to create a User with a null creationDate",
        nullCreationDateException.getMessage());

    assertDoesNotThrow(() -> new User(id, name, email, creationDate));
  }

  @Test
  void shouldCreateCorrectToString() {
    final var user = new User(id, name, email, creationDate);
    assertEquals("User {"
        + "\"creationDate\":\"2024-12-12 01:00\","
        + "\"email\":\"Email {\\\"email\\\":\\\"email@email.com\\\"}\","
        + "\"name\":\"Name {\\\"value\\\":\\\"any name\\\"}\","
        + "\"id\":\"Id {\\\"value\\\":\\\"any\\\"}\"}", user.toString());
  }

}