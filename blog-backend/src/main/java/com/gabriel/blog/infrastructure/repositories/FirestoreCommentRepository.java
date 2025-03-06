package com.gabriel.blog.infrastructure.repositories;

import com.gabriel.blog.application.repositories.CommentRepository;
import com.gabriel.blog.domain.entities.Comment;
import com.gabriel.blog.domain.valueobjects.Id;
import com.gabriel.blog.infrastructure.exceptions.RepositoryException;
import com.gabriel.blog.infrastructure.models.CommentModel;
import com.google.cloud.firestore.Firestore;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import org.jboss.logging.Logger;

/**
 * Firestore implementation of the {@link CommentRepository} interface.
 */
@ApplicationScoped
public class FirestoreCommentRepository implements CommentRepository {

  private static final String COLLECTION_NAME = "comments";
  private static final Logger logger = Logger.getLogger(FirestoreCommentRepository.class);

  private final Firestore firestore;

  /**
   * Constructs a new {@link FirestoreCommentRepository} instance.
   *
   * @param firestore the Firestore instance to use
   */
  public FirestoreCommentRepository(final Firestore firestore) {
    this.firestore = firestore;
  }

  @Override
  public void save(final Comment comment) {
    try {
      firestore.collection(COLLECTION_NAME)
          .document(comment.getId().getValue())
          .set(CommentModel.from(comment))
          .get();
    } catch (final Exception e) {
      Thread.currentThread().interrupt();
      logger.error("Failed to save comment to Firestore", e);
      throw new RepositoryException("Failed to save comment to Firestore", e);
    }
  }

  @Override
  public List<Comment> getCommentsById(final List<Id> ids) {
    try {
      final var futures = ids.stream()
          .map(id -> firestore.collection(COLLECTION_NAME).document(id.getValue()).get())
          .toList();

      final var comments = new ArrayList<Comment>();
      for (final var future : futures) {
        final var document = future.get();
        if (document.exists()) {
          comments.add(document.toObject(CommentModel.class).toDomain());
        }
      }
      return comments;
    } catch (final Exception e) {
      Thread.currentThread().interrupt();
      logger.error("Failed to retrieve comments from Firestore", e);
      throw new RepositoryException("Failed to retrieve comments from Firestore", e);
    }
  }
}
