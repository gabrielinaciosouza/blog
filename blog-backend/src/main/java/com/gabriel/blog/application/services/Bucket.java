package com.gabriel.blog.application.services;

import com.gabriel.blog.domain.valueobjects.Image;

/**
 * The {@link Bucket} interface defines the contract for managing image storage buckets in the system.
 *
 * <p>This interface allows different implementations of image storage buckets. The method
 * {@link #createImage(UploadImageParams)} takes image data and metadata and creates an image in the bucket.</p>
 *
 * <p>Implementations of this interface should handle the logic for managing image storage buckets,
 * which may vary depending on the requirements (e.g., Google Cloud Storage, Amazon S3, etc.).</p>
 *
 * <p>Created by Gabriel Inacio de Souza on February 2, 2025.</p>
 */
public interface Bucket {

  /**
   * Creates an image in the bucket based on the specified image data and metadata.
   *
   * @param params the image data and metadata for creating the image.
   * @return the created image as an {@link Image}.
   */
  Image createImage(UploadImageParams params);

  /**
   * Retrieves the bucket type associated with the bucket.
   *
   * @return the bucket type as a {@link BucketType}.
   */
  BucketType getBucket();

  /**
   * The {@link BucketType} enum represents the different types of image storage buckets in the system.
   *
   * <p>This enum contains the different types of image storage buckets that can be used to store images
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

    public static boolean existsType(final String bucketName) {
      for (final BucketType type : BucketType.values()) {
        if (type.getBucketName().equals(bucketName)) {
          return true;
        }
      }
      return false;
    }

    public String getBucketName() {
      return bucketName;
    }
  }

  /**
   * The {@link UploadImageParams} class represents the parameters required to upload an image to the bucket.
   *
   * <p>This class contains the image data, file name, and file MIME type required to upload an image to the bucket.</p>
   *
   * <p>Created by Gabriel Inacio de Souza on February 2, 2025.</p>
   */
  record UploadImageParams(byte[] fileData, String fileName, String fileMimeType) {
  }
}
