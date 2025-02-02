package com.gabriel.blog.businessrules.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabriel.blog.businessrules.repositories.PostRepository;
import com.gabriel.blog.businessrules.requests.CreatePostRequest;
import com.gabriel.blog.domain.entities.Post;
import com.gabriel.blog.domain.services.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CreatePostUseCaseTest {

  @Mock
  private IdGenerator idGeneratorMock;

  @Mock
  private PostRepository postRepositoryMock;

  private CreatePostUseCase createPostUseCase;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    createPostUseCase = new CreatePostUseCase(idGeneratorMock, postRepositoryMock);
  }

  @Test
  void shouldCreatePostSuccessfully() {
    final var title = "Test Title";
    final var content = "Test Content";
    final var createPostRequest = new CreatePostRequest(title, content);

    final var generatedId = "mocked-id";
    when(idGeneratorMock.generateId("posts")).thenReturn(generatedId);

    final var response = createPostUseCase.create(createPostRequest);

    verify(idGeneratorMock).generateId("posts");
    verify(postRepositoryMock).save(any(Post.class));


    assertNotNull(response);
    assertEquals(generatedId, response.postId());
    assertEquals(title, response.title());
    assertEquals(content, response.content());
    assertNotNull(response.creationDate());
  }

  @Test
  void shouldNotHandleErrorWhenRepositoryFails() {
    final var title = "Test Title";
    final var content = "Test Content";
    final var createPostRequest = new CreatePostRequest(title, content);


    final var generatedId = "mocked-id";
    when(idGeneratorMock.generateId("posts")).thenReturn(generatedId);

    doThrow(new RuntimeException("Repository error")).when(postRepositoryMock)
        .save(any(Post.class));

    final var exception =
        assertThrows(RuntimeException.class, () -> createPostUseCase.create(createPostRequest));

    assertEquals("Repository error", exception.getMessage());
  }
}
