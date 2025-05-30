package com.gabriel.blog.application.usecases;

import com.gabriel.blog.application.exceptions.AlreadyExistsException;
import com.gabriel.blog.application.exceptions.ValidationException;
import com.gabriel.blog.application.repositories.PostRepository;
import com.gabriel.blog.application.requests.CreatePostRequest;
import com.gabriel.blog.application.responses.PostResponse;
import com.gabriel.blog.application.services.IdGenerator;
import com.gabriel.blog.domain.entities.Post;
import com.gabriel.blog.domain.valueobjects.Content;
import com.gabriel.blog.domain.valueobjects.CreationDate;
import com.gabriel.blog.domain.valueobjects.DeletedStatus;
import com.gabriel.blog.domain.valueobjects.Id;
import com.gabriel.blog.domain.valueobjects.Image;
import com.gabriel.blog.domain.valueobjects.Slug;
import com.gabriel.blog.domain.valueobjects.Title;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Objects;

/**
 * The {@link CreatePostUseCase} class is responsible for handling the creation of a new blog post.
 * It coordinates the process of converting a {@link CreatePostRequest}
 * into a domain {@link Post} entity,
 * saving it using the {@link PostRepository}, and then returning a {@link PostResponse}.
 *
 * <p>Created by Gabriel Inacio de Souza on February 2, 2025.</p>
 *
 * <p>This use case encapsulates the business logic for creating a new post in the blog, including
 * the transformation of input data (from {@link CreatePostRequest} to {@link Post})
 * and the interaction with the repository to store the new post.</p>
 */
@ApplicationScoped
public class CreatePostUseCase {

  private static final String POSTS_DOMAIN = "posts";
  private final IdGenerator idGenerator;
  private final PostRepository postRepository;

  /**
   * Constructs a new {@link CreatePostUseCase} with the specified {@link PostRepository}.
   * This constructor allows the injection of the repository that will be responsible for persisting
   * the new post in the database.
   *
   * @param idGenerator    the responsible for generate ID; must not be {@code null}.
   * @param postRepository the repository that handles saving posts; must not be {@code null}.
   */
  public CreatePostUseCase(final IdGenerator idGenerator, final PostRepository postRepository) {
    this.idGenerator = idGenerator;
    this.postRepository = postRepository;
  }

  /**
   * Handles the creation of a new blog post.
   * This method converts the incoming {@link CreatePostRequest}
   * into a {@link Post} entity, saves it using the {@link PostRepository}, and then returns a
   * {@link PostResponse} containing the created post's information.
   *
   * <p>The method performs the following steps:</p>
   * <ol>
   *   <li>Converts the {@link CreatePostRequest} into a domain {@link Post} entity</li>
   *   <li>Saves the created post using the {@link PostRepository#save(Post)} method.</li>
   *   <li>Transforms saved {@link Post} entity into {@link PostResponse} and returns it.</li>
   * </ol>
   *
   * @param postRequest the request data to create a new post; must not be {@code null}.
   * @return a {@link PostResponse} containing the information of the newly created post.
   */
  public PostResponse create(final CreatePostRequest postRequest) {
    if (Objects.isNull(postRequest)) {
      throw new ValidationException("Tried to create a Post with null request");
    }

    final var title = new Title(postRequest.title());
    final var slug = new Slug(title);

    final var postOptional = postRepository.findBySlug(slug);

    if (postOptional.isPresent()) {
      throw new AlreadyExistsException("Post with slug " + slug.getValue() + " already exists");
    }

    final var content = new Content(postRequest.content());
    final var post = new Post(
        new Id(idGenerator.generateId(POSTS_DOMAIN)),
        title, content,
        CreationDate.now(),
        slug,
        postRequest.coverImage() == null ? null : new Image(postRequest.coverImage()),
        DeletedStatus.notDeleted());

    postRepository.save(post);

    return new PostResponse(
        post.getId().getValue(),
        post.getTitle().getValue(),
        post.getContent().getValue(),
        post.getCreationDate().toString(),
        post.getSlug().getValue(),
        post.getCoverImage().toString());
  }
}
