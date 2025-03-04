package com.gabriel.blog.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.gabriel.blog.application.requests.AddCommentRequest;
import com.gabriel.blog.fixtures.CreationDateFixture;
import com.gabriel.blog.infrastructure.models.PostModel;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.Header;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
class CommentResourceIntegrationTest {

  @Inject
  private Firestore firestore;

  @AfterEach
  void tearDown() throws InterruptedException {
    deleteTestPosts();
  }

  @Test
  void shouldAddComment() throws ExecutionException, InterruptedException {
    final var timestamp = Timestamp.ofTimeSecondsAndNanos(
        CreationDateFixture.creationDate().getValue().getEpochSecond(),
        CreationDateFixture.creationDate().getValue().getNano());
    firestore.collection("posts").document("postId").set(Map.of(
        "title", "title",
        "content", "content",
        "slug", "slug",
        "creationDate", timestamp,
        "coverImage", "https://example.com/image.jpg",
        "deleted", false));

    given()
        .when()
        .header(new Header("content-type", MediaType.APPLICATION_JSON))
        .body(new AddCommentRequest("postId", "authorId", "any comment"))
        .post("/comments")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(204);

    assertFalse(
        firestore.collection("posts").document("postId").get().get().toObject(PostModel.class)
            .getComments().isEmpty());
  }

  @Test
  void shouldNotAddCommentIfNotFindPost() {
    given()
        .when()
        .header(new Header("content-type", MediaType.APPLICATION_JSON))
        .body(new AddCommentRequest("postId", "authorId", "any comment"))
        .post("/comments")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(400)
        .body("error", equalTo("Validation error"))
        .body("message", equalTo("Post not found"));
  }

  private void deleteTestPosts() throws InterruptedException {
    firestore.collection("posts").listDocuments().forEach(DocumentReference::delete);
    Thread.sleep(1000);
  }
}
