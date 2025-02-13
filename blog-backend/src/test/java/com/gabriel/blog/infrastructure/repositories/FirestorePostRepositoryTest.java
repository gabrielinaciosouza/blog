package com.gabriel.blog.infrastructure.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabriel.blog.domain.entities.Post;
import com.gabriel.blog.fixtures.IdFixture;
import com.gabriel.blog.fixtures.PostFixture;
import com.gabriel.blog.infrastructure.exceptions.RepositoryException;
import com.gabriel.blog.infrastructure.models.PostModel;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FirestorePostRepositoryTest {

  private static final Post POST = PostFixture.post();

  private Firestore firestore;
  private CollectionReference collectionReference;
  private ApiFuture apiFuture;
  private DocumentReference documentReference;
  private Query query;
  private FirestorePostRepository firestorePostRepository;

  @BeforeEach
  void setUp() {
    firestore = mock(Firestore.class);
    collectionReference = mock(CollectionReference.class);
    apiFuture = mock(ApiFuture.class);
    documentReference = mock(DocumentReference.class);
    query = mock(Query.class);
    firestorePostRepository = new FirestorePostRepository(firestore);
  }

  @Test
  void shouldThrowRepositoryExceptionOnThreadFailure()
      throws ExecutionException, InterruptedException {
    when(firestore.collection("posts")).thenReturn(collectionReference);
    when(collectionReference.whereEqualTo("slug", POST.getSlug().getValue()))
        .thenReturn(query);
    when(query.get()).thenReturn(apiFuture);
    when(apiFuture.get()).thenThrow(InterruptedException.class);

    assertThrows(RepositoryException.class,
        () -> firestorePostRepository.findBySlug(POST.getSlug()));
  }

  @Nested
  class SaveTests {

    @Test
    void shouldSavePostCorrectly() throws ExecutionException, InterruptedException {
      final var id = IdFixture.withId("any").getValue();
      when(firestore.collection("posts")).thenReturn(collectionReference);
      when(collectionReference.add(any(PostModel.class)))
          .thenReturn(apiFuture);

      firestorePostRepository.save(POST);

      verify(firestore).collection("posts");
      verify(collectionReference).add(any(PostModel.class));
      verify(apiFuture).get();
    }

    @Test
    void shouldThrowRepositoryExceptionOnThreadFailure()
        throws ExecutionException, InterruptedException {
      final var id = IdFixture.withId("any").getValue();
      when(firestore.collection("posts")).thenReturn(collectionReference);
      when(collectionReference.add(any(PostModel.class)))
          .thenReturn(apiFuture);
      when(apiFuture.get()).thenThrow(InterruptedException.class);

      assertThrows(RepositoryException.class, () -> firestorePostRepository.save(POST));
    }
  }

  @Nested
  class FindBySlugTests {

    @Test
    void shouldGetPostBySlug() throws ExecutionException, InterruptedException {
      when(firestore.collection("posts")).thenReturn(collectionReference);
      when(collectionReference.whereEqualTo("slug", POST.getSlug().getValue()))
          .thenReturn(query);
      when(query.get()).thenReturn(apiFuture);

      final var querySnapshot = mock(QuerySnapshot.class);
      final var documentSnapshot = mock(QueryDocumentSnapshot.class);

      when(apiFuture.get()).thenReturn(querySnapshot);
      when(querySnapshot.getDocuments()).thenReturn(List.of(documentSnapshot));
      when(documentSnapshot.toObject(PostModel.class)).thenReturn(PostModel.from(POST));

      final var result = firestorePostRepository.findBySlug(POST.getSlug());

      verify(firestore).collection("posts");
      verify(collectionReference).whereEqualTo("slug", POST.getSlug().getValue());
      verify(query).get();
      verify(apiFuture).get();
      verify(querySnapshot).getDocuments();
      verify(documentSnapshot).toObject(PostModel.class);
      assertEquals(POST, result.get());
    }
  }
}