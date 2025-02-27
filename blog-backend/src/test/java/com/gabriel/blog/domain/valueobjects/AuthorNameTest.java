package com.gabriel.blog.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gabriel.blog.domain.exceptions.DomainException;
import org.junit.jupiter.api.Test;

class AuthorNameTest {

  @Test
  void shouldCreateCorrectAuthorName() {
    final var thrown = assertThrows(DomainException.class, () -> new AuthorName(null));
    assertEquals("Tried to create an AuthorName with a null value", thrown.getMessage());
    assertDoesNotThrow(() -> new AuthorName("any"));
  }

}