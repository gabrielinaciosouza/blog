package com.gabriel.blog.infrastructure.repositories;

import com.gabriel.blog.application.repositories.ImageBucketRepository;
import com.gabriel.blog.domain.valueobjects.Image;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * The {@link GcsImageBucketRepository} class represents a Google Cloud Storage (GCS) image bucket
 * repository.
 *
 * <p>This class provides the implementation for creating images in a GCS bucket. The method {@link
 * #createImage(UploadImageParams)} creates an image in the specified bucket based on the image data
 * and metadata provided in the request.</p>
 *
 * <p>Created by Gabriel Inacio de Souza on February 2, 2025.</p>
 */
@ApplicationScoped
public class GcsImageBucketRepository implements ImageBucketRepository {

  private final Storage storage;

  /**
   * Creates a new {@link GcsImageBucketRepository} with the specified GCS storage client.
   *
   * @param storage the GCS storage client to use for managing GCS buckets.
   */
  public GcsImageBucketRepository(final Storage storage) {
    this.storage = storage;
  }

  @Override
  public Image createImage(final UploadImageParams params) {
    final var bucketName = params.bucketName();

    final var bucket = getOrCreateBucket(bucketName);

    final var blob = bucket.create(params.fileName(), params.fileData(), params.fileMimeType());
    return new Image("https://storage.googleapis.com/" + bucketName + "/" + blob.getName());
  }

  private Bucket getOrCreateBucket(final String bucketName) {
    var bucket = storage.get(bucketName);
    if (bucket == null) {
      bucket = storage.create(BucketInfo.of(bucketName));
    }
    return bucket;
  }
}
