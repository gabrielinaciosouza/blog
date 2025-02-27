package com.gabriel.blog.fixtures;

import com.gabriel.blog.domain.valueobjects.AuthorName;

public class AuthorNameFixture {

  public static AuthorName authorName() {
    return new AuthorName("any author name");
  }
}
