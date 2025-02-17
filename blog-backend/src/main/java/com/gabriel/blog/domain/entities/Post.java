package com.gabriel.blog.domain.entities;

import com.gabriel.blog.domain.abstractions.AbstractEntity;
import com.gabriel.blog.domain.exceptions.DomainException;
import com.gabriel.blog.domain.valueobjects.Content;
import com.gabriel.blog.domain.valueobjects.CreationDate;
import com.gabriel.blog.domain.valueobjects.DeletedStatus;
import com.gabriel.blog.domain.valueobjects.Id;
import com.gabriel.blog.domain.valueobjects.Image;
import com.gabriel.blog.domain.valueobjects.Slug;
import com.gabriel.blog.domain.valueobjects.Title;
import java.time.Instant;

/**
 * Represents a blog post as an entity.
 * This class ensures that the title, content, creation date, and slug are not null.
 * It also provides methods to mark the post as deleted or not deleted.
 *
 * <p>Created by Gabriel Inacio de Souza on February 1, 2025.</p>
 */
public class Post extends AbstractEntity {

  private static final Image DEFAULT_IMAGE = new Image(
      "https://lh3.googleusercontent.com/fife/ALs6j_ECxtSUQHFEWGsuDQGC4idoDbrfKNvvpxOVTpRPp7cNuTMZIvCuRHneaFe5I4ZJWgLxtmDuOvAVP7mPk3Aw4R02jh1JnzeK9C4Hnj2JiRpYxRPYQVd3Mw0NbiKTxF8ivCW9EUt4y-BalNrrxvCazoHrSCcDooePezJdKgJIeu5jYLaYgNW9BF7LFHdzCGf-zwqexzfnXt4_Dig2M3Vf9x5IBCaql5ZdX6t_CupuT03m-Pv9YIlN_bS9udrKvJqbMW_oGLwC62tEawcDiuQ3IteYQnSCrruSOC0nJDAv81aOexKbFU6SNSnC6mJoKPHe7MUIMVJ8TKo_v9jlXlqS71q67tWX_FcmpTUO2uc7yDeF23RifkT5HvfhpliLt3RRPadaXw3ewx0laW_ecEVzZ2eDGmL3af2UBdQLwYEAKnMitACn6AxzgLXCcepm7wfGXPL7N6bLp4isL-_u6ies16RLTktl7Ne-IFpvNYtb3oA01N0GNDyfCap0bFvgy_1Xz5-Vk_usvVWZB1qjtZpO-OeQX3fioCWDmYRe8reI0wegZwQp7nB2RQ8qJz4wmwzPl8XJfoZPuHDufNZqtqXnfdk5JC-jip8ftLpaX1ZnYq_Nfez4oFjhVaQBwc1XMLJGxy-bpxsPf2v_BurhpO948bB5Vxy00TdPCWFPk_0mBLtcaYU4_MZNNKxDh0PCEVEHN0id4ewlALBX-vEK-h1SMs4a1AdYWAK3yhyjiRvm7LH3Epwwxz9qj8EWTJ4tB_Y3noZ3ybmhXB3DX-n4T_AXxbc4YHEO6bjRVFcEpleU3Dgi1M0GoOdrjFBFwOs9N6hWhOgkh-oA4n4TvtDZoXuPBfQ_ym9nV-nSQTXzb7yQA4d4ovUr7XJuNICx7cHoaiqcQo3VJc1ONoOJC8wORn9XYa0n8XhoBTkmPnnR8btfyFeeBMlBEoob1PwV5A1EQEJlMRBxyUkpfCV660IfW4zeXwMWTSFyuaZSrg3HjEqoU1ayC9zSCZMak6pkkbOlrvcDBxTOkqd2s3Okl-aIUCJoyJ9z6468KeiYwtmpRbio7bQig8RkK6BLTlFOGnzP538SwfLBfHchgGCdMUNYCbwClVxp0hL_dmGFxIgcaP-odBM0CkJW6l2b1z8J6lrIk-M99DTSNyQV0vf1J-jkP7XD-kzfHaVzef6NniiIQ5n52u3jZOkYiOltRdeNkdTQafz4r7HJa2TBlojSE3UIigcW1uhw48ezLJqHKB2cmKkPi8td3INcsGV2jC2bMacINeP_efXG2PyOd5JRe8kLsDn1V38-qT2Pk7yVu-psAyJ5IWJuVj2N5yjidyqyOtqQP8_orgaRDtg1sMilZ7rwjxtg8qX1VQf-v-rJlVRyAHOeZse_FKIK0cmqB1QXRaRN2AUPU9faBuY7HtGA_fw7Npd-O0O9NeZ-xD_Nb94iwm8rei7TeBOaf-F8ZNV4i3t4Zr12r9E6Y9eitfL6KTZ4WC9VMofT_4X9npThMtgqo0RZ72Uo2jBePrBvdNYkNQFXt5DIYCL2NaQToIy86EQrLpScFZw89vwP9SEX4EiorCO60mMJntWLIxi7nrm5IxMkuPShQGn__QPxb6ZqXzJjdPW7Hzr6NCWgXBJi37hY6RHtB4b1hEuYAmI5DsTuud50NdP9fgHgjcjTw0GDODdqkDQz7tzr8O4F=w1920-h877");

  private final Title title;
  private final Content content;
  private final CreationDate creationDate;
  private final Slug slug;
  private final Image coverImage;
  private DeletedStatus deletedStatus;

  /**
   * Constructs a {@code Post} instance with the given
   * title, content, creation date, slug, cover image, and deleted status.
   *
   * @param id            the post id, must not be null
   * @param title         the post title, must not be null
   * @param content       the post content, must not be null
   * @param creationDate  the post creation date, must not be null
   * @param slug          the post slug, must not be null
   * @param coverImage    the post cover image, must not be null
   * @param deletedStatus the post deleted status, must not be null
   * @throws DomainException if any of the provided parameters is null
   */
  public Post(final Id id, final Title title, final Content content,
              final CreationDate creationDate, final Slug slug, final Image coverImage,
              final DeletedStatus deletedStatus) {
    super(id);
    this.title = nonNull(title, "Tried to create a Post with a null title");
    this.content = nonNull(content, "Tried to create a Post with a null content");
    this.creationDate = nonNull(creationDate, "Tried to create a Post with a null creationDate");
    this.slug = nonNull(slug, "Tried to create a Post with a null slug");
    this.coverImage = coverImage == null ? DEFAULT_IMAGE : coverImage;
    this.deletedStatus = nonNull(deletedStatus, "Tried to create a Post with a null deletedStatus");
  }

  public Title getTitle() {
    return title;
  }

  public Content getContent() {
    return content;
  }

  public CreationDate getCreationDate() {
    return creationDate;
  }

  public Slug getSlug() {
    return slug;
  }

  public Image getCoverImage() {
    return coverImage;
  }

  public boolean isDeleted() {
    return deletedStatus.isDeleted();
  }

  /**
   * Marks the post as deleted.
   */
  public void markAsDeleted() {
    this.deletedStatus = DeletedStatus.deleted();
  }

  /**
   * Marks the post as not deleted.
   */
  public void markAsNotDeleted() {
    this.deletedStatus = DeletedStatus.notDeleted();
  }

  public Instant getDeletionDate() {
    return deletedStatus.getDeletionDate();
  }
}