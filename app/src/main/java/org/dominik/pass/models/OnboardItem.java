package org.dominik.pass.models;

public final class OnboardItem {
  final int imageId;
  final String title;
  final String message;

  public OnboardItem(int imageId, String title, String message) {
    this.imageId = imageId;
    this.title = title;
    this.message = message;
  }

  public int getImageId() {
    return imageId;
  }

  public String getTitle() {
    return title;
  }

  public String getMessage() {
    return message;
  }
}
