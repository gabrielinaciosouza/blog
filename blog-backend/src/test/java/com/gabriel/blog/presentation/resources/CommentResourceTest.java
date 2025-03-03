package com.gabriel.blog.presentation.resources;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.gabriel.blog.application.requests.AddCommentRequest;
import com.gabriel.blog.application.usecases.AddCommentUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommentResourceTest {

  private AddCommentUseCase addCommentUseCase;
  private CommentResource commentResource;

  @BeforeEach
  void setUp() {
    addCommentUseCase = mock(AddCommentUseCase.class);
    commentResource = new CommentResource(addCommentUseCase);
  }

  @Test
  void shouldAddCommentSuccessfully() {
    final var request = new AddCommentRequest("1", "Test comment", "user1");

    commentResource.addComment(request);

    verify(addCommentUseCase).addComment(request);
  }
}