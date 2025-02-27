package com.gabriel.blog.domain.valueobjects;

import com.gabriel.blog.domain.abstractions.AbstractValueObject;
import com.gabriel.blog.domain.exceptions.DomainException;

/**
 * Email value object.
 */
public class Email extends AbstractValueObject {

  private final String email;

  /**
   * Constructs an {@code Email} instance with the given email.
   *
   * @param email the email, must not be null
   * @throws DomainException if the provided email is null or invalid
   */
  public Email(final String email) {
    this.email = nonNull(email, "Tried to create an Email with a null value");
    if (!isValidEmail(email)) {
      throw new DomainException("Invalid email address: " + email);
    }
  }

  private static boolean isValidEmail(final String email) {
    return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
  }

  public String getEmail() {
    return email;
  }
}
