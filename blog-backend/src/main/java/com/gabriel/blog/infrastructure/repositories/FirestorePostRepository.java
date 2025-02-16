package com.gabriel.blog.infrastructure.repositories;

import com.gabriel.blog.application.repositories.PostRepository;
import com.gabriel.blog.domain.entities.Post;
import com.gabriel.blog.domain.valueobjects.Slug;
import com.gabriel.blog.infrastructure.exceptions.RepositoryException;
import com.gabriel.blog.infrastructure.models.PostModel;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
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


  @Override
  public void save(final Post post) {
    try {
      firestore.collection(COLLECTION_NAME)
          .add(PostModel.from(post))
          .get();
      logger.info("Post with id " + post.getId().getValue() + " saved successfully");
    } catch (final InterruptedException | ExecutionException e) {
      Thread.currentThread().interrupt();
      logger.error("Failed to save post to Firestore", e);
      throw new RepositoryException("Failed to save post to Firestore", e);
    }
  }

  @Override
  public Optional<Post> findBySlug(final Slug slug) {
    try {
      return firestore.collection(COLLECTION_NAME)
          .whereEqualTo("slug", slug.getValue())
          .get()
          .get()
          .getDocuments()
          .stream()
          .findFirst()
          .map(doc -> doc.toObject(PostModel.class))
          .map(PostModel::toDomain);
    } catch (final InterruptedException | ExecutionException e) {
      Thread.currentThread().interrupt();
      logger.error("Failed to find post by slug in Firestore", e);
      throw new RepositoryException("Failed to find post by slug in Firestore", e);
    }
  }

  @Override
  public List<Post> findPosts(final FindPostsParams params) {
    try {
      if (params == null) {
        throw new RepositoryException("Search parameters must not be null");
      }

      if (params.page() < 1) {
        throw new RepositoryException("Page must be greater than 0");
      }

      int size = params.size();
      if (params.size() < 1) {
        size = 10;
      }

      Query query = firestore.collection(COLLECTION_NAME);
      if (params.sortBy() != null) {
        query = query.orderBy(params.sortBy().name(),
            Query.Direction.valueOf(params.sortOrder().name()));
      }

      return query.offset((params.page() - 1) * size)
          .limit(size)
          .get()
          .get()
          .getDocuments()
          .stream()
          .map(doc -> doc.toObject(PostModel.class))
          .map(PostModel::toDomain)
          .toList();
    } catch (final InterruptedException | ExecutionException e) {
      Thread.currentThread().interrupt();
      logger.error("Failed to find posts in Firestore", e);
      throw new RepositoryException("Failed to find posts in Firestore", e);
    }
  }
}
