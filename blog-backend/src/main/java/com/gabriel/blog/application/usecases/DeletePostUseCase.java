package com.gabriel.blog.application.usecases;

import com.gabriel.blog.application.repositories.PostRepository;
import com.gabriel.blog.domain.entities.Post;
import com.gabriel.blog.domain.valueobjects.Slug;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Use case for deleting a blog post.
 *
 * <p>This use case is responsible for deleting a blog post from the database.
 * It receives the unique slug of the post to be deleted, and removes the post
 * from the database using the {@link PostRepository}.</p>
 *
 * <p>Created by Gabriel Inacio de Souza on February 2, 2025.</p>
 */
@ApplicationScoped
public class DeletePostUseCase {

  final PostRepository postRepository;

  /**
   * Constructs a new {@link DeletePostUseCase} use case with the provided {@link PostRepository}.
   *
   * @param postRepository the repository for managing {@link Post} entities;
   */
  public DeletePostUseCase(final PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  /**
   * Deletes a blog post from the database based on its unique slug.
   *
   * <p>This method deletes a blog post from the database based on its unique slug.
   * If a post with the given slug is found, it is removed from the database.
   * If no post with the given slug is found, the method does nothing.</p>
   *
   * @param slug the unique slug of the post to delete.
   */
  public void deletePost(final String slug) {
    final var postOptional = postRepository.findBySlug(Slug.fromString(slug));

    if (postOptional.isEmpty()) {
      return;
    }

    final var post = postOptional.get();

    post.markAsDeleted();
    postRepository.update(post);
  }

}
