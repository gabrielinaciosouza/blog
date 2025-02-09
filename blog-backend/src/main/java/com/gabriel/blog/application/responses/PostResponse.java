package com.gabriel.blog.application.responses;

import com.gabriel.blog.domain.entities.Post;

/**
 * Represents the response returned after creating a new {@link Post}.
 * This DTO (Data Transfer Object) is used to convey the data of the created post
 * back to the client in a structured format.
 *
 * <p>Created by Gabriel Inacio de Souza on February 2, 2025.</p>
 *
 * <p>This class provides a simple representation of the {@link Post} entity
 * containing the post ID, title, content, and creation date, making it suitable for
 * returning as a response to a client after successfully creating a post.</p>
 */
public record PostResponse(
    String postId,
    String title,
    String content,
    String creationDate,
    String slug) {
}
