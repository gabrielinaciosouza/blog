package com.gabriel.blog.application.usecases;

import com.gabriel.blog.application.exceptions.ValidationException;
import com.gabriel.blog.application.repositories.ImageBucketRepository;
import com.gabriel.blog.application.requests.UploadImageRequest;
import com.gabriel.blog.application.responses.ImageResponse;
import com.gabriel.blog.application.services.Bucket;

/**
 * The {@link UploadImageUseCase} class represents the use case for uploading images to a storage
 * bucket.
 *
 * <p>This class contains the business logic for uploading images to a storage bucket. The method
 * {@link #uploadImage(UploadImageRequest)} takes a request object with the image data and metadata
 * and uploads the image to the specified bucket.</p>
 *
 * <p>Created by Gabriel Inacio de Souza on February 2, 2025.</p>
 */
public class UploadImageUseCase {

  private final ImageBucketRepository imageBucketRepository;

  /**
   * Creates a new {@link UploadImageUseCase} with the specified image bucket repository.
   *
   * @param imageBucketRepository the image bucket repository to use for managing image storage buckets.
   */
  public UploadImageUseCase(final ImageBucketRepository imageBucketRepository) {
    this.imageBucketRepository = imageBucketRepository;
  }

  /**
   * Uploads an image to the specified storage bucket based on the request data.
   *
   * @param request the request object containing the image data and metadata.
   * @return the response object containing the URL of the uploaded image.
   */
  public ImageResponse uploadImage(final UploadImageRequest request) {
    validateRequest(request);

    final var bucket =
        imageBucketRepository.getOrCreateBucket(Bucket.BucketType.valueOf(request.bucketName()));

    final var image = bucket.createImage(new Bucket.UploadImageParams(
        request.fileData(),
        request.fileName(),
        request.fileMimeType()));

    return new ImageResponse(image.toString(), request.fileName());
  }

  private void validateRequest(final UploadImageRequest request) {
    if (request == null) {
      throw new ValidationException("Request must not be null");
    }

    if (request.fileData() == null || request.fileData().length == 0) {
      throw new ValidationException("Invalid file data");
    }

    if (request.fileName() == null || request.fileName().isBlank()) {
      throw new ValidationException("Invalid file name");
    }

    if (request.fileMimeType() == null || request.fileMimeType().isBlank()) {
      throw new ValidationException("Invalid file mime type");
    }

    if (!Bucket.BucketType.existsType(request.bucketName())) {
      throw new ValidationException("Invalid bucket name");
    }
  }
}