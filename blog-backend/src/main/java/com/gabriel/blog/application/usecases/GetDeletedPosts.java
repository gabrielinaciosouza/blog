package com.gabriel.blog.application.usecases;

import com.gabriel.blog.application.repositories.PostRepository;
import com.gabriel.blog.application.responses.PostResponse;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

/**
 * The {@link GetDeletedPosts} class represents the use case for retrieving deleted posts.
 *
 * <p>This class contains the business logic for retrieving deleted posts. The method {@link
 * #getDeletedPosts()} retrieves all posts that have been marked as deleted.</p>
 *
 * <p>Created by Gabriel Inacio de Souza on February 2, 2025.</p>
 */
@ApplicationScoped
public class GetDeletedPosts {

  private final PostRepository postRepository;

  /**
   * Creates a new {@link GetDeletedPosts} with the specified post repository.
   *
   * @param postRepository the post repository to use for managing post data.
   */
  public GetDeletedPosts(final PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  public List<PostResponse> getDeletedPosts() {
    return postRepository.getDeletedPosts()
        .stream()
        .map(post -> new PostResponse(post.getId().getValue(),
            post.getTitle().getValue(),
            post.getContent().getValue(),
            post.getCreationDate().toString(),
            post.getSlug().getValue(),
            post.getCoverImage().toString()))
        .toList();
  }
}
