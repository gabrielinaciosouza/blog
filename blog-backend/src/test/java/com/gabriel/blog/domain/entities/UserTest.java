package com.gabriel.blog.domain.entities;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gabriel.blog.domain.exceptions.DomainException;
import com.gabriel.blog.domain.valueobjects.Email;
import com.gabriel.blog.fixtures.IdFixture;
import org.junit.jupiter.api.Test;

class UserTest {

  @Test
  void shouldThrowDomainExceptionWhenIdIsNull() {
    final var exception = assertThrows(DomainException.class,
        () -> new User(null, new Email("user@example.com"), User.Role.USER));
    assertEquals("Tried to create an Entity with a null id", exception.getMessage());
  }

  @Test
  void shouldThrowDomainExceptionWhenEmailIsNull() {
    final var exception = assertThrows(DomainException.class,
        () -> new User(IdFixture.withId("123"), null, User.Role.USER));
    assertEquals("Tried to create a User with a null email", exception.getMessage());
  }

  @Test
  void shouldThrowDomainExceptionWhenRoleIsNull() {
    final var exception = assertThrows(DomainException.class,
        () -> new User(IdFixture.withId("123"), new Email("user@example.com"), null));
    assertEquals("Tried to create a User with a null role", exception.getMessage());
  }

  @Test
  void shouldCreateValidUser() {
    assertDoesNotThrow(() ->
        new User(IdFixture.withId("123"), new Email("valid@example.com"), User.Role.USER));
  }

  @Test
  void shouldCreateCorrectToString() {
    final var user =
        new User(IdFixture.withId("123"), new Email("user@example.com"), User.Role.USER);
    assertEquals(
        "User {\"email\":\"Email {\\\"email\\\":\\\"user@example.com\\\"}\","
            + "\"role\":\"USER\","
            + "\"id\":\"Id {\\\"value\\\":\\\"123\\\"}\"}",
        user.toString());
  }
}