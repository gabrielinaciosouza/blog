package com.gabriel.blog.infrastructure.repositories;

import com.gabriel.blog.application.repositories.PostRepository;
import com.gabriel.blog.domain.entities.Post;
import com.gabriel.blog.infrastructure.exceptions.RepositoryException;
import com.gabriel.blog.infrastructure.models.PostModel;
import com.google.cloud.firestore.Firestore;
import java.util.concurrent.ExecutionException;
import org.jboss.logging.Logger;

public class FirestorePostRepository implements PostRepository {

  private static final Logger logger = Logger.getLogger(FirestorePostRepository.class);
  private static final String COLLECTION_NAME = "posts";
  private final Firestore firestore;

  public FirestorePostRepository(final Firestore firestore) {
    this.firestore = firestore;
  }

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
