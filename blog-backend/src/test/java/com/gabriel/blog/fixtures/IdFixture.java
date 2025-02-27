package com.gabriel.blog.fixtures;

import com.gabriel.blog.domain.valueobjects.Id;

public class IdFixture {

  public static Id withId(final String id) {
    return new Id(id);
  }

  public static Id postId() {
    return new Id("post id");
  }

  public static Id authorId() {
    return new Id("author id");
  }
}
