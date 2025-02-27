package com.gabriel.blog.fixtures;

import com.gabriel.blog.domain.valueobjects.Email;

public class EmailFixture {

  public static Email email() {
    return new Email("email@email.com");
  }
}
