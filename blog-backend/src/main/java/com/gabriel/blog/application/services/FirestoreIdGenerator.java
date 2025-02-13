package com.gabriel.blog.application.services;

import com.gabriel.blog.domain.entities.Post;
import com.gabriel.blog.infrastructure.services.IdGenerator;
import com.google.cloud.firestore.Firestore;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Implementation of the {@link IdGenerator} interface using Firestore as the ID generation source.
 * This class generates unique identifiers based on Firestore's auto-generated document IDs,
 * which can be used as unique IDs for domain entities such as {@link Post} in the application.
 *
 * <p>Created by Gabriel Inacio de Souza on February 2, 2025.</p>
 *
 * <p>When invoked, this generator uses Firestore's document creation mechanism to generate IDs.
 * The {@link FirestoreIdGenerator} provides a simple and reliable way
 * to generate unique identifiers for entities,
 * while leveraging Firestore's built-in functionality for ID creation.</p>
 *
 * <p>This class is typically used in scenarios where entities such as {@link Post} need to
 * be assigned unique IDs automatically by Firestore. The generated IDs are based on Firestore's
 * document IDs and are guaranteed to be unique within the given Firestore collection.</p>
 */
@ApplicationScoped
public class FirestoreIdGenerator implements IdGenerator {

  private final Firestore firestore;

  /**
   * Constructs {@link FirestoreIdGenerator} instance using the provided {@link Firestore} instance.
   *
   * @param firestore the Firestore instance used to generate document IDs.
   */
  public FirestoreIdGenerator(final Firestore firestore) {
    this.firestore = firestore;
  }

  /**
   * Generates a unique identifier using Firestore's auto-generated document ID.
   *
   * <p>This method creates a document in the specified Firestore collection and uses the
   * auto-generated document ID as the unique identifier for the entity.</p>
   *
   * @param domain the domain or collection name where the ID will be generated.
   * @return a unique ID as a {@link String}.
   */
  @Override
  public String generateId(final String domain) {
    return firestore.collection(domain).document().getId();
  }
}
