package com.gabriel.blog.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
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
    clearTestData();
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
        .statusCode(200)
        .body("commentId", notNullValue())
        .body("content", equalTo("any comment"))
        .body("creationDate", notNullValue())
        .body("author.authorId", equalTo("any"))
        .body("author.name", equalTo("John Doe"))
        .body("author.email", equalTo("email@email.com"))
        .body("author.profilePicture", equalTo("http://image.com"));

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

  @Test
  void shouldGetCommentsById() {
    final var timestamp = Timestamp.ofTimeSecondsAndNanos(
        CreationDateFixture.creationDate().getValue().getEpochSecond(),
        CreationDateFixture.creationDate().getValue().getNano());
    firestore.collection("comments").document("commentId").set(Map.of(
        "authorId", "any",
        "content", "content",
        "creationDate", timestamp,
        "deleted", false));

    firestore.collection("comments").document("commentId2").set(Map.of(
        "authorId", "any",
        "content", "content2",
        "creationDate", timestamp,
        "deleted", false));

    given()
        .when()
        .header(new Header("content-type", MediaType.APPLICATION_JSON))
        .body(new String[] { "commentId", "commentId2" })
        .post("/comments/by-ids")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(200)
        .body("size()", equalTo(2))
        .body("[0].commentId", notNullValue())
        .body("[0].content", equalTo("content"))
        .body("[0].creationDate", notNullValue())
        .body("[0].author.authorId", equalTo("any"))
        .body("[0].author.name", equalTo("John Doe"))
        .body("[0].author.email", equalTo("email@email.com"))
        .body("[0].author.profilePicture", equalTo("http://image.com"))
        .body("[1].commentId", notNullValue())
        .body("[1].content", equalTo("content2"))
        .body("[0].creationDate", notNullValue())
        .body("[0].author.authorId", equalTo("any"))
        .body("[0].author.name", equalTo("John Doe"))
        .body("[0].author.email", equalTo("email@email.com"))
        .body("[0].author.profilePicture", equalTo("http://image.com"));
  }

  @Test
  void shouldReturnEmptyIfNotFound() {
    given()
        .when()
        .header(new Header("content-type", MediaType.APPLICATION_JSON))
        .body(new String[] { "commentId", "commentId2" })
        .post("/comments/by-ids")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(200)
        .body("isEmpty()", equalTo(true));
  }

  @Test
  void shouldReturnEmptyIfEmpty() {
    given()
        .when()
        .header(new Header("content-type", MediaType.APPLICATION_JSON))
        .post("/comments/by-ids")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(200)
        .body("isEmpty()", equalTo(true));
  }


  private void clearTestData() throws InterruptedException {
    firestore.collection("posts").listDocuments().forEach(DocumentReference::delete);
    firestore.collection("comments").listDocuments().forEach(DocumentReference::delete);
    Thread.sleep(1000);
  }
}
