package com.gabriel.blog.domain.entities;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gabriel.blog.domain.exceptions.DomainException;
import com.gabriel.blog.fixtures.EmailFixture;
import com.gabriel.blog.fixtures.IdFixture;
import com.gabriel.blog.fixtures.ImageFixture;
import com.gabriel.blog.fixtures.NameFixture;
import com.gabriel.blog.fixtures.UserFixture;
import org.junit.jupiter.api.Test;

class UserTest {

  @Test
  void shouldThrowDomainException() {
    var exception = assertThrows(DomainException.class,
        () -> UserFixture.UserBuilder.anUser().withId(null).build());
    assertEquals("Tried to create an Entity with a null id", exception.getMessage());

    exception = assertThrows(DomainException.class,
        () -> UserFixture.UserBuilder.anUser().withEmail(null).build());
    assertEquals("Tried to create a User with a null email", exception.getMessage());

    exception = assertThrows(DomainException.class,
        () -> UserFixture.UserBuilder.anUser().withRole(null).build());
    assertEquals("Tried to create a User with a null role", exception.getMessage());

    exception = assertThrows(DomainException.class,
        () -> UserFixture.UserBuilder.anUser().withName(null).build());
    assertEquals("Tried to create a User with a null name", exception.getMessage());
  }

  @Test
  void shouldCreateValidUser() {
    assertDoesNotThrow(() -> new User(IdFixture.withId("123"), EmailFixture.email(), User.Role.USER,
        NameFixture.name(),
        ImageFixture.image()));
  }

  @Test
  void shouldCreateCorrectToString() {
    assertEquals(
        "User {\"email\":\"Email {\\\"email\\\":\\\"default@example.com\\\"}\","
            + "\"name\":\"Name {\\\"value\\\":\\\"Default User\\\"}\","
            + "\"pictureUrl\":\"http:\\/\\/default.img\",\"role\":\"USER\","
            + "\"id\":\"Id {\\\"value\\\":\\\"default-id\\\"}\"}",
        UserFixture.DEFAULT_USER.toString());
  }
}