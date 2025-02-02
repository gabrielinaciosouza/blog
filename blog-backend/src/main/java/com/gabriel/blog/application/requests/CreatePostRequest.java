package com.gabriel.blog.application.requests;

import com.gabriel.blog.domain.entities.Post;

/**
 * Represents a request to create a new post.
 * This class contains the data required to create a new {@link Post} entity.
 *
 * <p>Created by Gabriel Inacio de Souza on February 2, 2025.</p>
 *
 * <p>This DTO (Data Transfer Object) is used to collect the necessary data from the client
 * for creating a new post in the blog system. It contains a title and content as strings.</p>
 */
public record CreatePostRequest(String title, String content) {

}

