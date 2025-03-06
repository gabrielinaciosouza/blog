package com.gabriel.blog.application.responses;

import com.gabriel.blog.domain.entities.Author;

/**
 * Represents the response returned after creating a new {@link Author}.
 * This DTO (Data Transfer Object) is used to convey the data of the created author
 * back to the client in a structured format.
 *
 * <p>Created by Gabriel Inacio de Souza on February 2, 2025.</p>
 *
 * <p>This class provides a simple representation of the {@link Author} entity
 * containing the author ID, name, and email, making it suitable for
 * returning as a response to a client after successfully creating an author.</p>
 */
public record AuthorResponse(
    String authorId,
    String name,
    String email,
    String profilePicture
) {
}
