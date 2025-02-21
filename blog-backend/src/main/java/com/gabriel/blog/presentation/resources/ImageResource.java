package com.gabriel.blog.presentation.resources;

import com.gabriel.blog.application.requests.UploadImageRequest;
import com.gabriel.blog.application.responses.ImageResponse;
import com.gabriel.blog.application.usecases.UploadImageUseCase;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;


/**
 * The {@link ImageResource} class represents the REST resource for uploading images to a storage.
 *
 * <p>This class defines a POST endpoint for uploading images to a storage bucket.
 * The endpoint accepts multipart form data containing
 * the image file data, file name, file MIME type, and bucket name.</p>
 *
 * <p>Created by Gabriel Inacio de Souza on February 2, 2025.</p>
 */
@Path("/files")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.MULTIPART_FORM_DATA)
public class ImageResource {

  private final UploadImageUseCase uploadImageUseCase;

  /**
   * Creates a new {@link ImageResource} with the specified upload image use case.
   *
   * @param uploadImageUseCase the upload image use case.
   */
  public ImageResource(final UploadImageUseCase uploadImageUseCase) {
    this.uploadImageUseCase = uploadImageUseCase;
  }

  /**
   * Uploads an image to the storage bucket based on the specified image data and metadata.
   *
   * @param request the request object containing the image data and metadata.
   * @return the response object containing the URL of the uploaded image.
   */
  @POST
  @Path("/images")
  public ImageResponse uploadImage(final UploadImageRequest request) {

    return uploadImageUseCase.uploadImage(request);
  }
}
