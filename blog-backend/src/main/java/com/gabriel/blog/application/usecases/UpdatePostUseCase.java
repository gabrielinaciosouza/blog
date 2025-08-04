package com.gabriel.blog.application.usecases;

import com.gabriel.blog.application.exceptions.NotFoundException;
import com.gabriel.blog.application.exceptions.ValidationException;
import com.gabriel.blog.application.repositories.PostRepository;
import com.gabriel.blog.application.requests.PostRequest;
import com.gabriel.blog.domain.valueobjects.Slug;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Objects;

/**
 * Use case for editing an existing blog post.
 * This class contains the logic to update a post based on the provided request data.
 *
 * <p>Created by Gabriel Inacio de Souza on August 4th, 2025.</p>
 */
@ApplicationScoped
public class UpdatePostUseCase {

  private final PostRepository postRepository;

  /**
   * Constructs an instance of UpdatePostUseCase with the specified PostRepository.
   *
   * @param postRepository The repository used to access and modify post data.
   */
  public UpdatePostUseCase(final PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  /**
   * Edits an existing post based on the provided request.
   *
   * @param request The request containing the updated data for the post.
   */
  public void updatePost(final String slug, final PostRequest request) {

    if (Objects.isNull(request)) {
      throw new ValidationException("Request cannot be null");
    }

    postRepository.findBySlug(Slug.fromString(slug))
        .ifPresentOrElse(postRepository::update, () -> {
          throw new NotFoundException("Post not found with slug: " + slug);
        });
  }
}

