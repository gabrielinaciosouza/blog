package com.gabriel.blog.infrastructure.repositories;

import com.gabriel.blog.application.repositories.PostRepository;
import com.gabriel.blog.domain.entities.Post;
import com.gabriel.blog.infrastructure.exceptions.RepositoryException;
import com.gabriel.blog.infrastructure.models.PostModel;
import com.google.cloud.firestore.Firestore;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.concurrent.ExecutionException;
import org.jboss.logging.Logger;

/**
 * Implementation of {@link PostRepository} that stores posts in Google Firestore.
 *
 * <p>This repository is responsible for persisting blog posts using Firestore.
 * It converts the domain {@link Post} entity into a {@link PostModel},
 * which is a Firestore-compatible data model, and saves it in the "posts" collection.
 * </p>
 *
 * <p>Created by Gabriel Inacio de Souza on February 2, 2025.</p>
 */
@ApplicationScoped
public class FirestorePostRepository implements PostRepository {

  private static final Logger logger = Logger.getLogger(FirestorePostRepository.class);
  private static final String COLLECTION_NAME = "posts";
  private final Firestore firestore;

  /**
   * Constructs a new {@link FirestorePostRepository} with the provided Firestore instance.
   *
   * @param firestore the Firestore database connection; must not be {@code null}.
   */
  public FirestorePostRepository(final Firestore firestore) {
    this.firestore = firestore;
  }

  /**
   * Saves a given {@link Post} into Firestore.
   *
   * <p>This method converts the domain {@link Post} entity into a {@link PostModel}
   * and persists it in the "posts" collection of Firestore. If the operation fails,
   * it logs the error and throws a {@link RepositoryException}.
   * </p>
   *
   * @param post the blog post to be saved; must not be {@code null}.
   * @throws RepositoryException if the save operation fails due to an execution or interruption error.
   */
  @Override
  public void save(final Post post) {
    try {
      firestore.collection(COLLECTION_NAME)
          .document(post.getId().getValue())
          .set(new PostModel(post))
          .get();
      logger.info("Post with id " + post.getId().getValue() + " saved successfully");
    } catch (final InterruptedException | ExecutionException e) {
      Thread.currentThread().interrupt();
      logger.error("Failed to save post to Firestore", e);
      throw new RepositoryException("Failed to save post to Firestore", e);
    }
  }
}
