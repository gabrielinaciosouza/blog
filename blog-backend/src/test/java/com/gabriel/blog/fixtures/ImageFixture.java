package com.gabriel.blog.fixtures;

import com.gabriel.blog.domain.valueobjects.Image;

public class ImageFixture {

  public static Image image() {
    return new Image("https://example.com/image.jpg");
  }
}
