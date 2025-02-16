package com.gabriel.blog.infrastructure.services;

/**
 * The {@link IdGenerator} interface defines the contract for generating unique identifiers (IDs)
 * for domain entities in the system.
 *
 * <p>This interface allows different implementations of ID generation strategies. The method
 * {@link #generateId(String)} takes a domain-specific identifier
 * and generates a unique ID based on the domain.</p>
 *
 * <p>Implementations of this interface should handle the logic for generating unique IDs,
 * which may vary depending on the requirements (e.g., ULID, UUID, Firestore auto-ID, etc.).</p>
 *
 * <p>Created by Gabriel Inacio de Souza on February 2, 2025.</p>
 */
public interface IdGenerator {

  /**
   * Generates a unique identifier based on the specified domain.
   *
   * @param domain the domain or context for which the ID is being generated. This could
   *               represent an entity type, collection, or any other domain-specific value.
   * @return a unique identifier as a {@link String}.
   */
  String generateId(final String domain);
}

