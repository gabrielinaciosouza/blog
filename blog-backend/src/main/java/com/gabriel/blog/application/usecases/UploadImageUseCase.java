package com.gabriel.blog.application.usecases;

import com.gabriel.blog.application.exceptions.ValidationException;
import com.gabriel.blog.application.qualifiers.RandomGenerator;
import com.gabriel.blog.application.repositories.ImageBucketRepository;
import com.gabriel.blog.application.requests.UploadImageRequest;
import com.gabriel.blog.application.responses.ImageResponse;
import com.gabriel.blog.application.services.IdGenerator;
import jakarta.enterprise.context.ApplicationScoped;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static com.gabriel.blog.application.repositories.ImageBucketRepository.BucketType;
import static com.gabriel.blog.application.repositories.ImageBucketRepository.UploadImageParams;

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
@ApplicationScoped
public class UploadImageUseCase {

  private final ImageBucketRepository imageBucketRepository;
  private final IdGenerator idGenerator;

  /**
   * Creates a new {@link UploadImageUseCase} with the specified image bucket repository.
   *
   * @param imageBucketRepository the image bucket repository to use for managing image storage buckets.
   */
  public UploadImageUseCase(final ImageBucketRepository imageBucketRepository,
                            @RandomGenerator final IdGenerator idGenerator) {
    this.imageBucketRepository = imageBucketRepository;
    this.idGenerator = idGenerator;
  }

  /**
   * Uploads an image to the specified storage bucket based on the request data.
   *
   * @param request the request object containing the image data and metadata.
   * @return the response object containing the URL of the uploaded image.
   */
  public ImageResponse uploadImage(final UploadImageRequest request) {
    validateRequest(request);

    final var fileName = URLEncoder.encode(idGenerator.generateId(request.fileName()),
        StandardCharsets.UTF_8);
    final var image =
        imageBucketRepository.createImage(new UploadImageParams(
            request.fileData(),
            fileName,
            request.fileMimeType(),
                BucketType.getBucketType(request.bucketName()).orElseThrow(() -> new ValidationException("Invalid bucket name"))));

    return new ImageResponse(image.toString(), fileName);
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

    if (request.bucketName() == null || request.bucketName().isBlank()) {
      throw new ValidationException("Invalid bucket name");
    }
  }
}
