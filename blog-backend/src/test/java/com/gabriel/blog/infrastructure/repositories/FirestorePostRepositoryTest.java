package com.gabriel.blog.infrastructure.repositories;

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
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FirestorePostRepositoryTest {

  private static final Post POST = PostFixture.post();

  private Firestore firestore;
  private CollectionReference collectionReference;
  private ApiFuture apiFuture;
  private DocumentReference documentReference;
  private FirestorePostRepository firestorePostRepository;

  @BeforeEach
  void setUp() {
    firestore = mock(Firestore.class);
    collectionReference = mock(CollectionReference.class);
    apiFuture = mock(ApiFuture.class);
    documentReference = mock(DocumentReference.class);
    firestorePostRepository = new FirestorePostRepository(firestore);
  }

  @Test
  void shouldSavePostCorrectly() throws ExecutionException, InterruptedException {
    final var id = IdFixture.withId("any").getValue();
    when(firestore.collection("posts")).thenReturn(collectionReference);
    when(collectionReference.document(id))
        .thenReturn(documentReference);
    when(documentReference.set(any(PostModel.class))).thenReturn(apiFuture);

    firestorePostRepository.save(POST);


    verify(firestore).collection("posts");
    verify(documentReference).set(any(PostModel.class));
    verify(collectionReference).document(id);
    verify(apiFuture).get();
  }

  @Test
  void shouldThrowRepositoryExceptionOnThreadFailure()
      throws ExecutionException, InterruptedException {
    final var id = IdFixture.withId("any").getValue();
    when(firestore.collection("posts")).thenReturn(collectionReference);
    when(collectionReference.document(id))
        .thenReturn(documentReference);
    when(documentReference.set(any(PostModel.class))).thenReturn(apiFuture);
    when(apiFuture.get()).thenThrow(InterruptedException.class);

    assertThrows(RepositoryException.class, () -> firestorePostRepository.save(POST));
  }
}