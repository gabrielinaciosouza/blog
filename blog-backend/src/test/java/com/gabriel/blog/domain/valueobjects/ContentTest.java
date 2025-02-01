package com.gabriel.blog.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gabriel.blog.domain.exceptions.DomainException;
import org.junit.jupiter.api.Test;

class ContentTest {

  @Test
  void shouldCreateCorrectContent() {
    final var thrown = assertThrows(DomainException.class, () -> new Content(null));
    assertEquals("Tried to create a Content with a null value", thrown.getMessage());
    assertDoesNotThrow(() -> new Content("any"));
  }
}
