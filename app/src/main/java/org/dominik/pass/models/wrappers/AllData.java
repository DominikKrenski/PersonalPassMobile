package org.dominik.pass.models.wrappers;

import org.dominik.pass.http.enums.DataType;

import java.util.UUID;

public final class AllData {
  private UUID publicId;
  private String entryTitle;
  private DataType type;

  public AllData(UUID publicId, String entryTitle, DataType type) {
    this.publicId = publicId;
    this.entryTitle = entryTitle;
    this.type = type;
  }

  public UUID getPublicId() {
    return publicId;
  }

  public void setPublicId(UUID publicId) {
    this.publicId = publicId;
  }

  public String getEntryTitle() {
    return entryTitle;
  }

  public void setEntryTitle(String entryTitle) {
    this.entryTitle = entryTitle;
  }

  public DataType getType() {
    return type;
  }

  public void setType(DataType type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "AllData{" +
      "publicId=" + publicId +
      ", entryTitle='" + entryTitle + '\'' +
      ", type=" + type +
      '}';
  }
}
