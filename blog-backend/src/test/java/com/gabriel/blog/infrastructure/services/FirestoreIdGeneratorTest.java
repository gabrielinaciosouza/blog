package com.gabriel.blog.infrastructure.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabriel.blog.application.services.FirestoreIdGenerator;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class FirestoreIdGeneratorTest {

  @Mock
  private Firestore firestoreMock;

  @Mock
  private CollectionReference collectionReferenceMock;

  @Mock
  private DocumentReference documentReferenceMock;

  private FirestoreIdGenerator firestoreIdGenerator;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    firestoreIdGenerator = new FirestoreIdGenerator(firestoreMock);

    when(firestoreMock.collection(anyString())).thenReturn(collectionReferenceMock);
    when(collectionReferenceMock.document()).thenReturn(documentReferenceMock);
    when(documentReferenceMock.getId()).thenReturn("mocked-id");
  }

  @Test
  void shouldGenerateUniqueId() {
    final var domain = "posts";

    final var generatedId = firestoreIdGenerator.generateId(domain);

    assertNotNull(generatedId);
    assertEquals("mocked-id", generatedId);
    verify(firestoreMock).collection(
        domain);
    verify(collectionReferenceMock).document();
    verify(documentReferenceMock).getId();
  }

  @Test
  void shouldThrowExceptionWhenFirestoreFails() {
    final var domain = "posts";

    when(firestoreMock.collection(anyString())).thenThrow(new RuntimeException("Firestore error"));

    final var exception =
        assertThrows(RuntimeException.class, () -> firestoreIdGenerator.generateId(domain));

    assertEquals("Firestore error", exception.getMessage());
  }
}
