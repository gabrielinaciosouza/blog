package com.gabriel.blog.infrastructure.config;

import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

/**
 * Configuration class for the Google Cloud Storage.
 */
@ApplicationScoped
public class StorageConfig {

  private Storage storage;

  /**
   * Initializes the Storage instance.
   */
  @PostConstruct
  public void init() {
    final String newUrl = System.getProperty("quarkus.google.cloud.storage.host-override");
    if (newUrl != null) {
      storage = StorageOptions.newBuilder()
          .setHost(newUrl)
          .build()
          .getService();
      String bucketName = "blog-images";
      storage.create(BucketInfo.of(bucketName));
    } else {
      storage = StorageOptions.getDefaultInstance().getService();
    }
  }

  /**
   * Producer method for the Storage instance.
   *
   * @return the Storage instance
   */
  @Produces
  @Singleton
  @Preferred
  public Storage storage() {
    return storage;
  }
}