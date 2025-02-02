package com.gabriel.blog.fixtures;

import com.gabriel.blog.domain.entities.Post;

public class PostFixture {

  public static Post post() {
    return new Post(
        IdFixture.withId("any"),
        TitleFixture.title(),
        ContentFixture.content(),
        CreationDateFixture.creationDate());
  }
}
