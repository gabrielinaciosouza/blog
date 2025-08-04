package com.gabriel.blog.presentation.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabriel.blog.application.requests.FindPostsRequest;
import com.gabriel.blog.application.requests.PostRequest;
import com.gabriel.blog.application.responses.FindPostsResponse;
import com.gabriel.blog.application.responses.PostResponse;
import com.gabriel.blog.application.usecases.CreatePostUseCase;
import com.gabriel.blog.application.usecases.DeletePostUseCase;
import com.gabriel.blog.application.usecases.FindPostsUseCase;
import com.gabriel.blog.application.usecases.GetDeletedPosts;
import com.gabriel.blog.application.usecases.GetPostBySlug;
import com.gabriel.blog.application.usecases.UpdatePostUseCase;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PostResourceTest {

  private CreatePostUseCase createPostUseCase;
  private GetPostBySlug getPostBySlug;
  private FindPostsUseCase findPostsUseCase;
  private DeletePostUseCase deletePostUseCase;
  private GetDeletedPosts getDeletedPosts;
  private UpdatePostUseCase updatePostUseCase;
  private PostResource postResource;

  @BeforeEach
  void setup() {
    createPostUseCase = mock(CreatePostUseCase.class);
    getPostBySlug = mock(GetPostBySlug.class);
    findPostsUseCase = mock(FindPostsUseCase.class);
    deletePostUseCase = mock(DeletePostUseCase.class);
    getDeletedPosts = mock(GetDeletedPosts.class);
    updatePostUseCase = mock(UpdatePostUseCase.class);
    postResource =
        new PostResource(createPostUseCase, getPostBySlug, findPostsUseCase, deletePostUseCase,
            getDeletedPosts, updatePostUseCase);
  }

  @Test
  void shouldCreatePostSuccessfully() {
    final var request = new PostRequest("title", "content", "https://example.com/image.jpg");
    final var expectedResponse =
        new PostResponse("id", "title", "content", "date", "slug", "https://example.com/image.jpg");
    when(createPostUseCase.create(request)).thenReturn(expectedResponse);

    final var response = postResource.create(request);

    assertEquals(expectedResponse, response);
    verify(createPostUseCase).create(request);
  }

  @Test
  void shouldGetPostBySlugSuccessfully() {
    final var slug = "slug";
    final var expectedResponse =
        new PostResponse("id", "title", "content", "date", "slug", "https://example.com/image.jpg");
    when(getPostBySlug.getPostBySlug(slug)).thenReturn(expectedResponse);

    final var response = postResource.getPostBySlug(slug);

    assertEquals(expectedResponse, response);
    verify(getPostBySlug).getPostBySlug(slug);
  }

  @Test
  void shouldFindPostsSuccessfully() {
    final var request = new FindPostsRequest(1, 10, "title", "ASCENDING");
    final var expectedResponse =
        new PostResponse("id", "title", "content", "date", "slug", "https://example.com/image.jpg");
    final var expectedPosts = List.of(expectedResponse);
    final var expectedTotal = 1;
    when(findPostsUseCase.findPosts(request)).thenReturn(
        new FindPostsResponse(expectedPosts, expectedTotal));

    final var response = postResource.findPosts(request);

    assertEquals(expectedPosts, response.posts());
    assertEquals(expectedTotal, response.totalCount());
    verify(findPostsUseCase).findPosts(request);
  }

  @Test
  void shouldDeletePostSuccessfully() {
    final var slug = "slug";
    postResource.deletePost(slug);
    verify(deletePostUseCase).deletePost(slug);
  }

  @Test
  void shouldGetDeletedPostsSuccessfully() {
    final var expectedResponse =
        new PostResponse("id", "title", "content", "date", "slug", "https://example.com/image.jpg");
    final var expectedPosts = List.of(expectedResponse);
    when(getDeletedPosts.getDeletedPosts()).thenReturn(expectedPosts);

    final var response = postResource.getDeletedPosts();

    assertEquals(expectedPosts, response);
    verify(getDeletedPosts).getDeletedPosts();
  }

  @Test
  void shouldUpdatePostSuccessfully() {
    final var slug = "slug";
    final var request = new PostRequest("updated title", "updated content",
        "https://example.com/updated-image.jpg");

    doNothing().when(updatePostUseCase).updatePost(slug, request);

    postResource.updatePost(slug, request);

    verify(updatePostUseCase).updatePost(slug, request);
  }
}
