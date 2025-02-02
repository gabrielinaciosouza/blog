package com.gabriel.blog.fixtures;

import com.gabriel.blog.domain.valueobjects.Content;

public class ContentFixture {

  public static Content content() {
    return new Content("any content");
  }
}
