package com.gabriel.blog.fixtures;

import com.gabriel.blog.domain.valueobjects.Name;

public class NameFixture {

  public static Name name() {
    return new Name("any name");
  }
}
