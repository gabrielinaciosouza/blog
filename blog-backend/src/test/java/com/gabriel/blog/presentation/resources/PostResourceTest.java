package com.gabriel.blog.presentation.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabriel.blog.application.requests.CreatePostRequest;
import com.gabriel.blog.application.requests.FindPostsRequest;
import com.gabriel.blog.application.responses.FindPostsResponse;
import com.gabriel.blog.application.responses.PostResponse;
import com.gabriel.blog.application.usecases.CreatePostUseCase;
import com.gabriel.blog.application.usecases.FindPostsUseCase;
import com.gabriel.blog.application.usecases.GetPostBySlug;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PostResourceTest {

  private CreatePostUseCase createPostUseCase;
  private GetPostBySlug getPostBySlug;
  private PostResource postResource;
  private FindPostsUseCase findPostsUseCase;

  @BeforeEach
  void setup() {
    createPostUseCase = mock(CreatePostUseCase.class);
    getPostBySlug = mock(GetPostBySlug.class);
    findPostsUseCase = mock(FindPostsUseCase.class);
    postResource = new PostResource(createPostUseCase, getPostBySlug, findPostsUseCase);
  }

  @Test
  void shouldCreatePostSuccessfully() {
    final var request = new CreatePostRequest("title", "content");
    final var expectedResponse = new PostResponse("id", "title", "content", "date", "slug");
    when(createPostUseCase.create(request)).thenReturn(expectedResponse);

    final var response = postResource.create(request);

    assertEquals(expectedResponse, response);
    verify(createPostUseCase).create(request);
  }

  @Test
  void shouldGetPostBySlugSuccessfully() {
    final var slug = "slug";
    final var expectedResponse = new PostResponse("id", "title", "content", "date", "slug");
    when(getPostBySlug.getPostBySlug(slug)).thenReturn(expectedResponse);

    final var response = postResource.getPostBySlug(slug);

    assertEquals(expectedResponse, response);
    verify(getPostBySlug).getPostBySlug(slug);
  }

  @Test
  void shouldFindPostsSuccessfully() {
    final var request = new FindPostsRequest(1, 10, "title", "ASCENDING");
    final var expectedResponse = new PostResponse("id", "title", "content", "date", "slug");
    final var expectedPosts = List.of(expectedResponse);
    final var expectedTotal = 1;
    when(findPostsUseCase.findPosts(request)).thenReturn(
        new FindPostsResponse(expectedPosts, expectedTotal));

    final var response = postResource.findPosts(request);

    assertEquals(expectedPosts, response.posts());
    assertEquals(expectedTotal, response.totalCount());
    verify(findPostsUseCase).findPosts(request);
  }
}
