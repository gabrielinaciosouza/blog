package com.gabriel.blog.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gabriel.blog.domain.exceptions.DomainException;
import org.junit.jupiter.api.Test;

class ImageTest {

  @Test
  void shouldCreateImageWithValidUrl() {
    final var validUrl = "https://example.com/image.jpg";
    final var image = new Image(validUrl);
    assertEquals(validUrl, image.getValue().toString());
  }

  @Test
  void shouldThrowExceptionForInvalidUrl() {
    final var invalidUrl = "invalid-url";
    assertThrows(DomainException.class, () -> new Image(invalidUrl));
  }

  @Test
  void shouldThrowExceptionForNullUrl() {
    assertThrows(DomainException.class, () -> new Image(null));
  }

  @Test
  void shouldReturnUrlAsString() {
    final var validUrl = "https://example.com/image.jpg";
    final var image = new Image(validUrl);
    assertEquals(validUrl, image.toString());
  }
}