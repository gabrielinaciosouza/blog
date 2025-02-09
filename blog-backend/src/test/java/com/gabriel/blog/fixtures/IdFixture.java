package com.gabriel.blog.fixtures;

import com.gabriel.blog.domain.valueobjects.Id;

public class IdFixture {

  public static Id withId(final String id) {
    return new Id(id);
  }
}
