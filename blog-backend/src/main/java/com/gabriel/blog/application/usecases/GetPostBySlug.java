package com.gabriel.blog.application.usecases;

import com.gabriel.blog.application.exceptions.NotFoundException;
import com.gabriel.blog.application.repositories.PostRepository;
import com.gabriel.blog.application.responses.PostResponse;
import com.gabriel.blog.domain.valueobjects.Slug;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Use case for retrieving a blog post by its slug.
 *
 * <p>Created by Gabriel Inacio de Souza on February 9, 2025.</p>
 */
@ApplicationScoped
public class GetPostBySlug {

  private final PostRepository postRepository;

  /**
   * Constructs a new {@link GetPostBySlug} use case with the provided {@link PostRepository}.
   *
   * @param postRepository the repository used to retrieve the post
   */
  public GetPostBySlug(final PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  /**
   * Retrieves a blog post by its slug.
   *
   * @param slug the slug of the post to retrieve
   * @return a {@link PostResponse} containing the post data
   */
  public PostResponse getPostBySlug(final String slug) {
    final var postOptional = postRepository.findBySlug(Slug.fromString(slug));

    if (postOptional.isEmpty()) {
      throw new NotFoundException("Post with slug " + slug + " not found");
    }

    final var post = postOptional.get();
    return new PostResponse(
        post.getId().getValue(),
        post.getTitle().getValue(),
        post.getContent().getValue(),
        post.getCreationDate().toString(),
        post.getSlug().getValue());
  }
}
