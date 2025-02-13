package com.gabriel.blog.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gabriel.blog.domain.exceptions.DomainException;
import org.junit.jupiter.api.Test;

class SlugTest {

  @Test
  void shouldCreateValidSlug() {
    final Slug slug = new Slug(new Title("How to Create a Blog!"));
    assertEquals("how-to-create-a-blog", slug.getValue());
  }

  @Test
  void shouldHandleAccents() {
    final Slug slug = new Slug(new Title("Café com Açúcar"));
    assertEquals("cafe-com-acucar", slug.getValue());
  }

  @Test
  void shouldReplaceMultipleSpacesWithSingleHyphen() {
    final Slug slug = new Slug(new Title("  Multiple    Spaces   "));
    assertEquals("multiple-spaces", slug.getValue());
  }

  @Test
  void shouldRemoveSpecialCharacters() {
    final Slug slug = new Slug(new Title("SEO & Best Practices in 2025!"));
    assertEquals("seo-best-practices-in-2025", slug.getValue());
  }

  @Test
  void shouldConvertToLowerCase() {
    final Slug slug = new Slug(new Title("UpperCase Title"));
    assertEquals("uppercase-title", slug.getValue());
  }

  @Test
  void shouldThrowExceptionForNullValue() {
    final var thrown = assertThrows(DomainException.class, () -> new Slug(null));
    assertEquals("Tried to create a Slug with a null value", thrown.getMessage());
  }

  @Test
  void shouldCreateSlugFromString() {
    final Slug slug = Slug.fromString("any-slug");
    assertEquals("any-slug", slug.getValue());
  }
}
