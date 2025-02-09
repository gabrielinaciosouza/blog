package com.gabriel.blog.domain.valueobjects;

import com.gabriel.blog.domain.abstractions.AbstractValueObject;
import com.gabriel.blog.domain.exceptions.DomainException;

/**
 * Represents the content of a post, ensuring it is not null.
 * This class is a value object following domain-driven design principles.
 *
 * <p>Created by Gabriel Inacio de Souza on February 1, 2025.</p>
 */
public class Content extends AbstractValueObject {

  final String value;

  /**
   * Constructs a {@code Content} instance with the given value.
   *
   * @param value the content value, must not be null
   * @throws DomainException if the provided value is null
   */
  public Content(final String value) {
    this.value = nonNull(value, "Tried to create a Content with a null value");
  }

  public String getValue() {
    return value;
  }
}
