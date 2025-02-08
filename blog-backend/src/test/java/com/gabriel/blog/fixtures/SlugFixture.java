package com.gabriel.blog.fixtures;

import com.gabriel.blog.domain.valueobjects.Slug;

public class SlugFixture {

  public static Slug slug() {
    return new Slug(TitleFixture.title());
  }
}
