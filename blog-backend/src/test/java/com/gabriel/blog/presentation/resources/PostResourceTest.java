package com.gabriel.blog.presentation.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabriel.blog.application.repositories.PostRepository;
import com.gabriel.blog.application.requests.CreatePostRequest;
import com.gabriel.blog.application.responses.PostResponse;
import com.gabriel.blog.application.usecases.CreatePostUseCase;
import com.gabriel.blog.application.usecases.GetPostBySlug;
import com.gabriel.blog.fixtures.CreationDateFixture;
import com.gabriel.blog.fixtures.PostFixture;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PostResourceTest {

  private CreatePostUseCase createPostUseCase;
  private GetPostBySlug getPostBySlug;
  private PostResource postResource;
  private PostRepository postRepository;

  @BeforeEach
  void setup() {
    createPostUseCase = mock(CreatePostUseCase.class);
    getPostBySlug = mock(GetPostBySlug.class);
    postRepository = mock(PostRepository.class);
    postResource = new PostResource(createPostUseCase, getPostBySlug, postRepository);
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
    final var params = new PostRepository.FindPostsParams(0, 10, PostRepository.SortBy.title,
        PostRepository.SortOrder.ASCENDING);
    final var post = PostFixture.post();
    final var expectedResponse = List.of(
        new PostResponse("any", "any title", "any content", CreationDateFixture.creationDate()
            .toString(),
            "any-title"));
    when(postRepository.findPosts(params)).thenReturn(List.of(post));

    final var response = postResource.findPosts(params);

    assertEquals(expectedResponse, response);
    verify(postRepository).findPosts(params);
  }
}
