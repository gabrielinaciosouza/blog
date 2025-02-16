package com.gabriel.blog.domain.valueobjects;

import com.gabriel.blog.domain.abstractions.AbstractValueObject;
import com.gabriel.blog.domain.exceptions.DomainException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Represents the creation date of an entity as a value object.
 * This class ensures that the date is not null and provides a method to get the current date.
 *
 * <p>Created by Gabriel Inacio de Souza on February 1, 2025.</p>
 */
public class CreationDate extends AbstractValueObject {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
      .withZone(ZoneId.systemDefault());
  private final Instant value;

  /**
   * Constructs a {@code CreationDate} instance with the given date.
   *
   * @param value the creation date, must not be null
   * @throws DomainException if the provided date is null
   */
  public CreationDate(final Instant value) {
    this.value = nonNull(value, "Tried to create a CreationDate with a null value");
  }

  /**
   * Creates a {@code CreationDate} instance with the current date.
   *
   * @return a new {@code CreationDate} set to the current date
   */
  public static CreationDate now() {
    return new CreationDate(Instant.now());
  }

  public Instant getValue() {
    return value;
  }

  @Override
  public String toString() {
    return FORMATTER.format(value);
  }
}