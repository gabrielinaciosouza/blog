package com.gabriel.blog.domain.valueobjects;

import com.gabriel.blog.domain.abstractions.AbstractValueObject;

/**
 * Represents the name of an author, ensuring it is not null.
 * This class is a value object following domain-driven design principles.
 *
 * <p>Created by Gabriel Inacio de Souza on February 27, 2025.</p>
 */
public class AuthorName extends AbstractValueObject {

  final String value;

  /**
   * Constructs an {@code AuthorName} instance with the given value.
   *
   * @param value the author name value, must not be null
   */
  public AuthorName(final String value) {
    this.value = nonNull(value, "Tried to create an AuthorName with a null value");
  }

  public String getValue() {
    return value;
  }
}
