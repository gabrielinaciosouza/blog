package com.gabriel.blog.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gabriel.blog.domain.exceptions.DomainException;
import org.junit.jupiter.api.Test;

class EmailTest {

  @Test
  void shouldCreateCorrectEmail() {
    final var thrown = assertThrows(DomainException.class, () -> new Email(null));
    assertEquals("Tried to create an Email with a null value", thrown.getMessage());
    final var thrown2 = assertThrows(DomainException.class, () -> new Email("invalid"));
    assertEquals("Invalid email address: invalid", thrown2.getMessage());
    assertDoesNotThrow(() -> new Email("email@email.com"));
  }
}