package com.gabriel.blog.application.repositories;

import com.gabriel.blog.domain.valueobjects.Image;

public interface ImageBucketRepository {

	/**
	 * Creates an image in the bucket based on the specified image data and metadata.
	 *
	 * @param params the image data and metadata for creating the image.
	 * @return the created image as an {@link Image}.
	 */
	Image createImage(UploadImageParams params);

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

	record UploadImageParams(byte[] fileData, String fileName, String fileMimeType, String bucketName) {
	}
}
