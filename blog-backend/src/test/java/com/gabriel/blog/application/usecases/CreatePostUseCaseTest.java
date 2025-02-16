package com.gabriel.blog.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabriel.blog.application.exceptions.AlreadyExistsException;
import com.gabriel.blog.application.exceptions.ValidationException;
import com.gabriel.blog.application.repositories.PostRepository;
import com.gabriel.blog.application.requests.CreatePostRequest;
import com.gabriel.blog.domain.entities.Post;
import com.gabriel.blog.domain.valueobjects.Slug;
import com.gabriel.blog.fixtures.PostFixture;
import com.gabriel.blog.infrastructure.services.IdGenerator;
import java.util.Optional;
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
    when(postRepositoryMock.findBySlug(Slug.fromString("test-title"))).thenReturn(Optional.empty());

    final var response = createPostUseCase.create(createPostRequest);

    verify(idGeneratorMock).generateId("posts");
    verify(postRepositoryMock).save(any(Post.class));


    assertNotNull(response);
    assertEquals(generatedId, response.postId());
    assertEquals(title, response.title());
    assertEquals(content, response.content());
    assertNotNull(response.creationDate());
    assertEquals(response.slug(), "test-title");
  }

  @Test
  void shouldNotHandleErrorWhenRepositoryFails() {
    final var title = "Test Title";
    final var content = "Test Content";
    final var createPostRequest = new CreatePostRequest(title, content);


    final var generatedId = "mocked-id";
    when(idGeneratorMock.generateId("posts")).thenReturn(generatedId);
    when(postRepositoryMock.findBySlug(Slug.fromString("test-title"))).thenReturn(Optional.empty());


    doThrow(new RuntimeException("Repository error")).when(postRepositoryMock)
        .save(any(Post.class));

    final var exception =
        assertThrows(RuntimeException.class, () -> createPostUseCase.create(createPostRequest));

    assertEquals("Repository error", exception.getMessage());
  }

  @Test
  void shouldThrowValidationErrorWhenRequestIsNull() {
    final var exception =
        assertThrows(ValidationException.class, () -> createPostUseCase.create(null));

    assertEquals("Tried to create a Post with null request", exception.getMessage());
  }

  @Test
  void shouldThrowAlreadyExistsExceptionWhenPostAlreadyExists() {
    final var title = "Test Title";
    final var content = "Test Content";
    final var createPostRequest = new CreatePostRequest(title, content);

    when(postRepositoryMock.findBySlug(Slug.fromString("test-title")))
        .thenReturn(Optional.of(PostFixture.post()));

    final var exception =
        assertThrows(AlreadyExistsException.class,
            () -> createPostUseCase.create(createPostRequest));

    assertEquals("Post with slug test-title already exists", exception.getMessage());
  }
}
