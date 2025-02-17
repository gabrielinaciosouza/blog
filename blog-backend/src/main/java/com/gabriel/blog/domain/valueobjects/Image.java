package com.gabriel.blog.domain.valueobjects;

import com.gabriel.blog.domain.abstractions.AbstractValueObject;
import com.gabriel.blog.domain.exceptions.DomainException;
import java.net.URI;
import java.net.URL;

/**
 * Represents an image URL as a value object.
 * This class ensures that the URL is valid.
 *
 * <p>Created by Gabriel Inacio de Souza on February 1, 2025.</p>
 */
public class Image extends AbstractValueObject {

  private final URL value;

  /**
   * Constructs an {@code Image} instance with the given URL.
   *
   * @param value the image URL, must not be null and must be a valid URL
   * @throws DomainException if the provided URL is null or invalid
   */
  public Image(final String value) {
    this.value = validateUrl(value);
  }

  private URL validateUrl(final String value) {
    try {
      final var uri = new URI(nonNull(value, "Tried to create an Image with a null value"));
      return uri.toURL();
    } catch (final Exception e) {
      throw new DomainException("Tried to create an Image with an invalid URL: " + value);
    }
  }

  public URL getValue() {
    return value;
  }

  @Override
  public String toString() {
    return value.toString();
  }
}