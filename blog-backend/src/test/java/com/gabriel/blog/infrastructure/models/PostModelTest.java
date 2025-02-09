package com.gabriel.blog.infrastructure.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.gabriel.blog.fixtures.PostFixture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import org.junit.jupiter.api.Test;

class PostModelTest {

  @Test
  void shouldReturnPostModelFromEntity() {
    final var post = PostFixture.post();
    final var postModel = PostModel.from(post);

    assertEquals(post.getTitle().getValue(), postModel.getTitle());
    assertEquals(post.getContent().getValue(), postModel.getContent());
    assertEquals(post.getCreationDate().toString(), postModel.getCreationDate());
    assertEquals(post.getSlug().getValue(), postModel.getSlug());
  }

  @Test
  void shouldReturnPostFromQueryDocumentSnapshot() {
    final var queryDocumentSnapshot = mock(QueryDocumentSnapshot.class);
    when(queryDocumentSnapshot.toObject(PostModel.class)).thenReturn(
        new PostModel("title", "content", "2025-02-02", "slug"));
    when(queryDocumentSnapshot.getId()).thenReturn("any");

    final var post = PostModel.toDomain(queryDocumentSnapshot);

    assertEquals("title", post.getTitle().getValue());
    assertEquals("content", post.getContent().getValue());
    assertEquals("2025-02-02", post.getCreationDate().toString());
    assertEquals("slug", post.getSlug().getValue());
    assertEquals("any", post.getId().getValue());
  }
}