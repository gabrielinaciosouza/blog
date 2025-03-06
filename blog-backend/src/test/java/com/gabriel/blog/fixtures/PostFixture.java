package com.gabriel.blog.fixtures;

import com.gabriel.blog.domain.entities.Post;
import com.gabriel.blog.domain.valueobjects.DeletedStatus;
import java.util.ArrayList;

public class PostFixture {

  public static Post post() {
    return new Post(
        IdFixture.withId("any"),
        TitleFixture.title(),
        ContentFixture.content(),
        CreationDateFixture.creationDate(),
        SlugFixture.slug(),
        ImageFixture.image(),
        DeletedStatus.notDeleted(),
        new ArrayList<>());
  }

  public static Post deletedPost() {
    return new Post(
        IdFixture.withId("any"),
        TitleFixture.title(),
        ContentFixture.content(),
        CreationDateFixture.creationDate(),
        SlugFixture.slug(),
        ImageFixture.image(),
        DeletedStatus.deleted(),
        new ArrayList<>());
  }
}
