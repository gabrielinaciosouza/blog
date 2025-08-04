package com.gabriel.blog.application.requests;

/**
 * Represents a request to create or update a blog post.
 * This record encapsulates the necessary data for creating or updating a post,
 * including the title, content, and cover image.
 *
 * <p>Created by Gabriel Inacio de Souza on February 2, 2025.</p>
 */
public record PostRequest(String title, String content, String coverImage) {

}

