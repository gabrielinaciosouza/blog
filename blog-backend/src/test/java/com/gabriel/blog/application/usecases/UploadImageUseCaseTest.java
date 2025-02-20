package com.gabriel.blog.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabriel.blog.application.exceptions.ValidationException;
import com.gabriel.blog.application.repositories.ImageBucketRepository;
import com.gabriel.blog.application.requests.UploadImageRequest;
import com.gabriel.blog.application.services.IdGenerator;
import com.gabriel.blog.domain.valueobjects.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UploadImageUseCaseTest {

  private ImageBucketRepository imageBucketRepository;
  private IdGenerator idGenerator;
  private UploadImageUseCase uploadImageUseCase;

  @BeforeEach
  void setUp() {
    imageBucketRepository = mock(ImageBucketRepository.class);
    idGenerator = mock(IdGenerator.class);
    uploadImageUseCase = new UploadImageUseCase(imageBucketRepository, idGenerator);
  }

  UploadImageRequest validRequest() {
    final var fileData = "fileData".getBytes();
    final var fileName = "fileName.jpg";
    final var fileMimeType = "image/jpeg";
    final var bucketName = "content-images";

    return new UploadImageRequest(fileData, fileName, fileMimeType, bucketName);
  }

  @Test
  void uploadImageSuccessfully() {
    final var request = validRequest();
    final var image = new Image("http://example.com/image.jpg");
    final var generatedFileName = "generatedFileName.jpg";

    when(idGenerator.generateId(request.fileName())).thenReturn(generatedFileName);
    when(imageBucketRepository.createImage(
        any(ImageBucketRepository.UploadImageParams.class))).thenReturn(image);

    final var result = uploadImageUseCase.uploadImage(request);

    assertNotNull(result);
    assertEquals("http://example.com/image.jpg", result.url());
    assertEquals(generatedFileName, result.fileName());
    verify(imageBucketRepository).createImage(any(ImageBucketRepository.UploadImageParams.class));
  }

  @Test
  void uploadImageWithNullRequestThrowsException() {
    assertThrows(ValidationException.class, () -> uploadImageUseCase.uploadImage(null));
  }

  @Test
  void uploadImageWithInvalidFileDataThrowsException() {
    final var request =
        new UploadImageRequest(null, "fileName.jpg", "image/jpeg", "bucketName");
    assertThrows(ValidationException.class, () -> uploadImageUseCase.uploadImage(request));
    final var requestWithEmptyFileData =
        new UploadImageRequest(new byte[0], "fileName.jpg", "image/jpeg", "bucketName");
    assertThrows(ValidationException.class,
        () -> uploadImageUseCase.uploadImage(requestWithEmptyFileData));
  }

  @Test
  void uploadImageWithInvalidFileNameThrowsException() {
    final var request =
        new UploadImageRequest("fileData".getBytes(), "", "image/jpeg", "bucketName");
    assertThrows(ValidationException.class, () -> uploadImageUseCase.uploadImage(request));
    final var requestWithNullFileName =
        new UploadImageRequest("fileData".getBytes(), null, "image/jpeg", "bucketName");
    assertThrows(ValidationException.class,
        () -> uploadImageUseCase.uploadImage(requestWithNullFileName));
  }

  @Test
  void uploadImageWithInvalidFileMimeTypeThrowsException() {
    final var request =
        new UploadImageRequest("fileData".getBytes(), "fileName.jpg", "", "bucketName");
    assertThrows(ValidationException.class, () -> uploadImageUseCase.uploadImage(request));
    final var requestWithNullFileMimeType =
        new UploadImageRequest("fileData".getBytes(), "fileName.jpg", null, "bucketName");
    assertThrows(ValidationException.class,
        () -> uploadImageUseCase.uploadImage(requestWithNullFileMimeType));
  }

  @Test
  void uploadImageWithInvalidBucketNameThrowsException() {
    final var request =
        new UploadImageRequest("fileData".getBytes(), "fileName.jpg", "image/jpeg",
            "invalidBucketName");
    assertThrows(ValidationException.class, () -> uploadImageUseCase.uploadImage(request));
    final var requestWithNullBucketName =
        new UploadImageRequest("fileData".getBytes(), "fileName.jpg", "image/jpeg", null);
    assertThrows(ValidationException.class,
        () -> uploadImageUseCase.uploadImage(requestWithNullBucketName));
  }
}