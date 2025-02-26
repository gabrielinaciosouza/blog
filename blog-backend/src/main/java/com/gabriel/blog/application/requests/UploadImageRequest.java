package com.gabriel.blog.application.requests;

import jakarta.ws.rs.FormParam;

/**
 * The {@link UploadImageRequest} class represents the request object for uploading an image to a storage bucket.
 *
 * <p>This class contains the image data, file name, file MIME type, and bucket name required to upload an image to the bucket.</p>
 *
 * <p>Created by Gabriel Inacio de Souza on February 2, 2025.</p>
 */
public record UploadImageRequest(
    @FormParam("file")
    byte[] fileData,
    @FormParam("fileName")
    String fileName,
    @FormParam("fileMimeType")
    String fileMimeType,
    @FormParam("bucketName")
    String bucketName) {
}
