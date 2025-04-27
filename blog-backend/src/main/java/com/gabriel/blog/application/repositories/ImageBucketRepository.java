package com.gabriel.blog.application.repositories;

import com.gabriel.blog.domain.valueobjects.Image;
import java.util.Optional;

/**
 * The {@link ImageBucketRepository} interface represents the repository for managing image buckets.
 *
 * <p>This interface contains the methods for creating images in the storage bucket based
 * on the specified image data and metadata.</p>
 *
 * <p>Created by Gabriel Inacio de Souza on February 2, 2025.</p>
 */
public interface ImageBucketRepository {

  /**
   * Creates an image in the bucket based on the specified image data and metadata.
   *
   * @param params the image data and metadata for creating the image.
   * @return the created image as an {@link Image}.
   */
  Image createImage(UploadImageParams params);

  /**
   * The {@link BucketType} enum represents the different types of image buckets in the system.
   *
   * <p>This enum contains the different types of image buckets that can be used to store images
   * in the system. Each bucket type has a unique name that identifies the bucket in the system.</p>
   *
   * <p>Created by Gabriel Inacio de Souza on February 2, 2025.</p>
   */
  enum BucketType {
    COVER_IMAGES("cover-images"),
    CONTENT_IMAGES("content-images");

    private final String bucketName;

    BucketType(final String bucketName) {
      this.bucketName = bucketName;
    }

    public String getBucketName() {
      return bucketName;
    }

    public static Optional<BucketType> getBucketType(final String bucketName) {
      for (final BucketType type : BucketType.values()) {
        if (type.getBucketName().equals(bucketName)) {
          return Optional.of(type);
        }
      }
      return Optional.empty();
    }
  }

  /**
   * The {@link UploadImageParams} record represents the parameters required to upload an image
   * to a storage bucket.
   *
   * <p>This record contains the image data, file name, file MIME type, and bucket name required to
   * upload an image
   * to the storage bucket.</p>
   *
   * <p>Created by Gabriel Inacio de Souza on February 2, 2025.</p>
   */
  record UploadImageParams(byte[] fileData, String fileName, String fileMimeType,
                           BucketType bucketType) {
  }
}
