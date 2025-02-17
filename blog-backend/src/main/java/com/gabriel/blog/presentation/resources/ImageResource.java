package com.gabriel.blog.presentation.resources;

import com.gabriel.blog.infrastructure.config.Preferred;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;
import java.util.UUID;


@Path("/images")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.MULTIPART_FORM_DATA)
public class ImageResource {

  @Inject
  @Preferred
  Storage storage;

  @POST
  public Response uploadImage(
      @FormParam("file") byte[] fileData,
      @FormParam("fileName") String fileName,
      @FormParam("fileMimeType") String fileMimeType) {

    String bucketName = "blog-images";


    String uniqueFileName = UUID.randomUUID().toString() + "-" + fileName;
    BlobId blobId = BlobId.of(bucketName, uniqueFileName);
    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(fileMimeType).build();

    storage.create(blobInfo, fileData);

    final String newUrl = System.getProperty("quarkus.google.cloud.storage.host-override");
    String publicUrl = newUrl + bucketName + "/" + uniqueFileName;
    return Response.ok(Map.of("url", publicUrl)).build();
  }
}