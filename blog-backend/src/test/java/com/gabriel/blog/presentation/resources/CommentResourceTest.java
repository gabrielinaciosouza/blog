package com.gabriel.blog.presentation.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabriel.blog.application.requests.AddCommentRequest;
import com.gabriel.blog.application.responses.AuthorResponse;
import com.gabriel.blog.application.responses.CommentResponse;
import com.gabriel.blog.application.usecases.AddCommentUseCase;
import com.gabriel.blog.application.usecases.GetCommentsByIdUseCase;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommentResourceTest {

  private AddCommentUseCase addCommentUseCase;
  private GetCommentsByIdUseCase getCommentsByIdUseCase;
  private CommentResource commentResource;

  @BeforeEach
  void setUp() {
    addCommentUseCase = mock(AddCommentUseCase.class);
    getCommentsByIdUseCase = mock(GetCommentsByIdUseCase.class);
    commentResource = new CommentResource(addCommentUseCase, getCommentsByIdUseCase);
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

  @Test
  void shouldGetCommentsByIdSuccessfully() {
    final List<String> commentIds = Arrays.asList("1", "2");
    final List<CommentResponse> expectedResponses = Arrays.asList(
        new CommentResponse("1", "Comment 1", "creation date 1",
            new AuthorResponse("author id 1", "user1", "email1", "image1")),
        new CommentResponse("2", "Comment 2", "creation date 2",
            new AuthorResponse("author id 2", "user2", "email2", "image2"))
    );

    when(getCommentsByIdUseCase.getCommentsById(commentIds)).thenReturn(expectedResponses);

    final var responses = commentResource.getCommentsById(commentIds);

    verify(getCommentsByIdUseCase).getCommentsById(commentIds);
    assertEquals(expectedResponses, responses);
  }
}