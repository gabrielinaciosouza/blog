package com.gabriel.blog.application.responses;

/**
 * The {@link ImageResponse} class represents the response object for an image upload operation.
 *
 * <p>This class contains the URL of the uploaded image and the file name of the image.</p>
 *
 * <p>Created by Gabriel Inacio de Souza on February 2, 2025.</p>
 */
public record ImageResponse(String url, String fileName) {
}
