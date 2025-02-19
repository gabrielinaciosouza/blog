package com.gabriel.blog.infrastructure.repositories;

import com.gabriel.blog.application.repositories.ImageBucketRepository;
import com.gabriel.blog.domain.valueobjects.Image;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GcsImageBucketRepository implements ImageBucketRepository {

	private final Storage storage;

	public GcsImageBucketRepository(Storage storage) {
		this.storage = storage;
	}

	@Override
	public Image createImage(final UploadImageParams params) {
		final var bucketName = params.bucketName();

		var bucket = getOrCreateBucket(bucketName);

		final var blob = bucket.create(params.fileName(), params.fileData(), params.fileMimeType());
		return new Image("https://storage.googleapis.com/" + bucketName + "/" + blob.getName());
	}

	private Bucket getOrCreateBucket(String bucketName) {
		var bucket = storage.get(bucketName);
		if (bucket == null) {
			bucket = storage.create(BucketInfo.of(bucketName));
		}
		return bucket;
	}
}
