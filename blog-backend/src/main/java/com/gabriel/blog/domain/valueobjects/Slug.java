package com.gabriel.blog.domain.valueobjects;

import com.gabriel.blog.domain.abstractions.AbstractValueObject;
import com.gabriel.blog.domain.exceptions.DomainException;
import java.text.Normalizer;
import java.util.Locale;

/**
 * Represents the slug of a post, ensuring it is not null.
 * This class is a value object following domain-driven design principles.
 *
 * <p>Created by Gabriel Inacio de Souza on February 1, 2025.</p>
 */
public class Slug extends AbstractValueObject {

  final String value;

  /**
   * Constructs a {@code Slug} instance with the given value.
   *
   * @param title the content value, must not be null
   * @throws DomainException if the provided value is null
   */
  public Slug(final Title title) {
    nonNull(title, "Tried to create a Slug with a null value");
    this.value = toSlug(title);
  }

  private String toSlug(final Title title) {

    final String normalized = Normalizer.normalize(title.getValue(), Normalizer.Form.NFD);
    return normalized.replaceAll("[^\\p{ASCII}]", "")
        .replaceAll("[^a-zA-Z0-9\\s-]", "")
        .trim()
        .replaceAll("\\s+", "-")
        .replaceAll("-{2,}", "-")
        .toLowerCase(Locale.ROOT);
  }

  public String getValue() {
    return value;
  }
}
