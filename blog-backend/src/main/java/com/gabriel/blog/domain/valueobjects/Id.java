package com.gabriel.blog.domain.valueobjects;

import com.gabriel.blog.domain.AbstractValueObject;
import com.gabriel.blog.domain.exceptions.DomainException;

/**
 * Represents an identifier for a domain object.
 * This class ensures that the identifier is non-null when created.
 *
 * <p>Created by Gabriel Inacio de Souza on February 1, 2025.</p>
 */
public class Id extends AbstractValueObject {

  private final String value;

  /**
   * Constructs an {@code Id} object with the specified value.
   *
   * @param value the value of the identifier
   * @throws DomainException if the value is {@code null}
   */
  public Id(final String value) {
    this.value = nonNull(value, "Tried to create an id with a null value");
  }

  /**
   * Retrieves the value of the identifier.
   *
   * @return the value of the identifier
   */
  public String getValue() {
    return value;
  }
}

