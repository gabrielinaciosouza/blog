package com.gabriel.blog.presentation.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabriel.blog.application.requests.UploadImageRequest;
import com.gabriel.blog.application.responses.ImageResponse;
import com.gabriel.blog.application.usecases.UploadImageUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImageResourceTest {

  private UploadImageUseCase uploadImageUseCase;
  private ImageResource imageResource;

  @BeforeEach
  void setUp() {
    uploadImageUseCase = mock(UploadImageUseCase.class);
    imageResource = new ImageResource(uploadImageUseCase);
  }

  @Test
  void uploadImageSuccessfully() {
    final var fileData = "fileData".getBytes();
    final var fileName = "fileName.jpg";
    final var fileMimeType = "image/jpeg";
    final var bucketName = "bucketName";
    final var request = new UploadImageRequest(fileData, fileName, fileMimeType, bucketName);
    final var response = new ImageResponse("http://example.com/image.jpg", "image.jpg");

    when(uploadImageUseCase.uploadImage(request)).thenReturn(response);

    final var result = imageResource.uploadImage(request);

    assertEquals(response, result);
    verify(uploadImageUseCase).uploadImage(request);
  }
}