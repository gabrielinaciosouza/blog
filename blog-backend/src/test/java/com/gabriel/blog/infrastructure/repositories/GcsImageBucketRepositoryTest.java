package com.gabriel.blog.infrastructure.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabriel.blog.application.repositories.ImageBucketRepository;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GcsImageBucketRepositoryTest {

  private static final String GCS_URL = "https://storage.googleapis.com/";
  private Storage storage;
  private GcsImageBucketRepository gcsImageBucketRepository;

  @BeforeEach
  void setUp() {
    storage = mock(Storage.class);
    gcsImageBucketRepository = new GcsImageBucketRepository(storage, GCS_URL);
  }

  @Test
  void createImageSuccessfully() {
    final var bucketType = ImageBucketRepository.BucketType.CONTENT_IMAGES;
    final var bucketName = bucketType.toString();
    final var fileName = "fileName.jpg";
    final var fileData = "fileData".getBytes();
    final var fileMimeType = "image/jpeg";
    final var params =
        new ImageBucketRepository.UploadImageParams(fileData, fileName, fileMimeType, bucketType);
    final var bucket = mock(Bucket.class);
    final var blob = mock(com.google.cloud.storage.Blob.class);

    when(storage.get(bucketName)).thenReturn(bucket);
    when(bucket.create(fileName, fileData, fileMimeType)).thenReturn(blob);
    when(blob.getName()).thenReturn(fileName);

    final var result = gcsImageBucketRepository.createImage(params);

    assertNotNull(result);
    assertEquals(GCS_URL + bucketName + "/o/" + fileName + "?alt=media",
        result.toString());
    verify(storage).get(bucketName);
    verify(bucket).create(fileName, fileData, fileMimeType);
  }

  @Test
  void createImageWithNonExistentBucketCreatesBucket() {
    final var bucketType = ImageBucketRepository.BucketType.CONTENT_IMAGES;
    final var bucketName = bucketType.toString();
    final var fileName = "fileName.jpg";
    final var fileData = "fileData".getBytes();
    final var fileMimeType = "image/jpeg";
    final var params =
        new ImageBucketRepository.UploadImageParams(fileData, fileName, fileMimeType, bucketType);
    final var bucket = mock(Bucket.class);
    final var blob = mock(com.google.cloud.storage.Blob.class);

    when(storage.get(bucketName)).thenReturn(null);
    when(storage.create(BucketInfo.of(bucketName))).thenReturn(bucket);
    when(bucket.create(fileName, fileData, fileMimeType)).thenReturn(blob);
    when(blob.getName()).thenReturn(fileName);

    final var result = gcsImageBucketRepository.createImage(params);

    assertNotNull(result);
    assertEquals(GCS_URL + bucketType + "/o/" + fileName + "?alt=media",
        result.toString());
    verify(storage).get(bucketName);
    verify(storage).create(BucketInfo.of(bucketName));
    verify(bucket).create(fileName, fileData, fileMimeType);
  }
}
