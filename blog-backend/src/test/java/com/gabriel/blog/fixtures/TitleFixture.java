package com.gabriel.blog.fixtures;

import com.gabriel.blog.domain.valueobjects.Title;

public class TitleFixture {

  public static Title title() {
    return new Title("any title");
  }
}
