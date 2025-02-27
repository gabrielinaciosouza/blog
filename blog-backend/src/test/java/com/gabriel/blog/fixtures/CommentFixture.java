package com.gabriel.blog.fixtures;

import com.gabriel.blog.domain.entities.Comment;
import com.gabriel.blog.domain.valueobjects.DeletedStatus;

public class CommentFixture {

  public static Comment comment() {
    return new Comment(IdFixture.withId("any"), IdFixture.authorId(), ContentFixture.content(),
        CreationDateFixture.creationDate(), DeletedStatus.notDeleted());
  }
}
