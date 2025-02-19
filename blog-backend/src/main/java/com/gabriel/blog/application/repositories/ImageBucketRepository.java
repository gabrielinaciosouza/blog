package com.gabriel.blog.application.repositories;

import com.gabriel.blog.application.services.Bucket;

/**
 * The {@link ImageBucketRepository} interface defines the contract for managing image storage
 * buckets in the system.
 *
 * <p>This interface allows different implementations of image storage bucket repositories. The method
 * {@link #getOrCreateBucket(Bucket.BucketType)} takes a bucket type and retrieves or creates a bucket
 * based on the type.</p>
 *
 * <p>Implementations of this interface should handle the logic for managing image storage buckets,
 * which may vary depending on the requirements (e.g., Google Cloud Storage, Amazon S3, etc.).</p>
 *
 * <p>Created by Gabriel Inacio de Souza on February 2, 2025.</p>
 */
public interface ImageBucketRepository {

  /**
   * Retrieves or creates an image storage bucket based on the specified bucket type.
   *
   * <p>This method retrieves an existing bucket or creates a new bucket based on the specified
   * bucket type. The bucket type is used to determine the storage location and configuration
   * for the image storage bucket.</p>
   *
   * @param bucketType the type of bucket to retrieve or create.
   * @return the image storage bucket as a {@link Bucket}.
   */
  Bucket getOrCreateBucket(final Bucket.BucketType bucketType);

}
