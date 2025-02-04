package com.gabriel.blog.presentation.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabriel.blog.application.requests.CreatePostRequest;
import com.gabriel.blog.application.responses.CreatePostResponse;
import com.gabriel.blog.application.usecases.CreatePostUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PostResourceTest {

  private CreatePostUseCase createPostUseCase;
  private PostResource postResource;

  @BeforeEach
  void setup() {
    createPostUseCase = mock(CreatePostUseCase.class);
    postResource = new PostResource(createPostUseCase);
  }

  @Test
  void shouldCreatePostSuccessfully() {
    final var request = new CreatePostRequest("title", "content");
    final var expectedResponse = new CreatePostResponse("id", "title", "content", "date");
    when(createPostUseCase.create(request)).thenReturn(expectedResponse);

    final var response = postResource.create(request);

    assertEquals(expectedResponse, response);
    verify(createPostUseCase).create(request);
  }
}
