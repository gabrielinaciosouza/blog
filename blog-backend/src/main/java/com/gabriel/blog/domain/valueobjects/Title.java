package com.gabriel.blog.domain.valueobjects;

import com.gabriel.blog.domain.AbstractValueObject;
import com.gabriel.blog.domain.exceptions.DomainException;


/**
 * Represents the title of a blog post.
 * Title is a value object that holds a non-null string value.
 *
 * <p>Created by Gabriel Inacio de Souza on February 1, 2025.</p>
 */
public class Title extends AbstractValueObject {

  private final String value;

  /**
   * Constructs a {@code Title} object with the specified value.
   *
   * @param value the title value
   * @throws DomainException if the value is {@code null}
   */
  public Title(final String value) {
    this.value = nonNull(value, "Tried to create a Title with a null value");
  }
}
