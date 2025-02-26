package com.gabriel.blog.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.filter.log.LogDetail;
import java.io.IOException;
import org.junit.jupiter.api.Test;

@QuarkusTest
class ImageResourceIntegrationTest {

  @Test
  void shouldReturn200WhenCreatingImage() throws IOException {
    given()
        .header("Content-Type", "multipart/form-data")
        .multiPart("file", "image.png",
            ImageResourceIntegrationTest.class.getResourceAsStream("/image.png").readAllBytes(),
            "image/png")
        .multiPart("fileName", "image.png")
        .multiPart("fileMimeType", "image/png")
        .multiPart("bucketName", "content-images")
        .post("/files/images")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(200)
        .body("url", notNullValue())
        .body("fileName", notNullValue());
  }

  @Test
  void shouldReturn400WhenCreatingImageWithInvalidFileParam() {
    given()
        .header("Content-Type", "multipart/form-data")
        .multiPart("fileName", "image.png")
        .multiPart("fileMimeType", "image/png")
        .multiPart("bucketName", "content-images")
        .post("/files/images")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(400)
        .body("message", equalTo("Invalid file data"));
  }

  @Test
  void shouldReturn400WhenCreatingImageWithInvalidFileName() throws IOException {
    given()
        .header("Content-Type", "multipart/form-data")
        .multiPart("file", "image.png",
            ImageResourceIntegrationTest.class.getResourceAsStream("/image.png").readAllBytes(),
            "image/png")
        .multiPart("fileMimeType", "image/png")
        .multiPart("bucketName", "content-images")
        .post("/files/images")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(400)
        .body("message", equalTo("Invalid file name"));
  }

  @Test
  void shouldReturn400WhenCreatingImageWithInvalidFileMimeType() throws IOException {
    given()
        .header("Content-Type", "multipart/form-data")
        .multiPart("file", "image.png",
            ImageResourceIntegrationTest.class.getResourceAsStream("/image.png").readAllBytes(),
            "image/png")
        .multiPart("fileName", "image.png")
        .multiPart("bucketName", "content-images")
        .post("/files/images")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(400)
        .body("message", equalTo("Invalid file mime type"));
  }

  @Test
  void shouldReturn400WhenCreatingImageWithInvalidBucketName() throws IOException {
    given()
        .header("Content-Type", "multipart/form-data")
        .multiPart("file", "image.png",
            ImageResourceIntegrationTest.class.getResourceAsStream("/image.png").readAllBytes(),
            "image/png")
        .multiPart("fileName", "image.png")
        .multiPart("fileMimeType", "image/png")
        .multiPart("bucketName", "any")
        .post("/files/images")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(400)
        .body("message", equalTo("Invalid bucket name"));

    given()
        .header("Content-Type", "multipart/form-data")
        .multiPart("file", "image.png",
            ImageResourceIntegrationTest.class.getResourceAsStream("/image.png").readAllBytes(),
            "image/png")
        .multiPart("fileMimeType", "image/png")
        .multiPart("fileName", "image.png")
        .post("/files/images")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(400)
        .body("message", equalTo("Invalid bucket name"));
  }

}
