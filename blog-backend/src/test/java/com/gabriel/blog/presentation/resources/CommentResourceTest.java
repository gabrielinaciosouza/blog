package com.gabriel.blog.presentation.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabriel.blog.application.requests.AddCommentRequest;
import com.gabriel.blog.application.responses.AuthorResponse;
import com.gabriel.blog.application.responses.CommentResponse;
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
    final var request = new AddCommentRequest("1", "Test comment", "content");
    final var expectedResponse =
        new CommentResponse("1",
            "Test comment",
            "creation date",
            new AuthorResponse("author id", "user1", "email", "any image"));

    when(addCommentUseCase.addComment(request)).thenReturn(expectedResponse);

    final var response = commentResource.addComment(request);

    verify(addCommentUseCase).addComment(request);
    assertEquals(expectedResponse, response);
  }
}