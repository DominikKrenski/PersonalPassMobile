package org.dominik.pass.http.dto;

import org.dominik.pass.http.enums.DataType;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class DataDTO {
  private UUID publicId;
  private String entry;
  private DataType type;
  Instant createdAt;
  Instant updatedAt;

  public UUID getPublicId() {
    return publicId;
  }

  public void setPublicId(UUID publicId) {
    this.publicId = publicId;
  }

  public String getEntry() {
    return entry;
  }

  public void setEntry(String entry) {
    this.entry = entry;
  }

  public DataType getType() {
    return type;
  }

  public void setType(DataType type) {
    this.type = type;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DataDTO dataDTO = (DataDTO) o;
    return publicId.equals(dataDTO.publicId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(publicId);
  }

  @Override
  public String toString() {
    return "DataDTO{" +
      "publicId=" + publicId +
      ", entry='" + entry + '\'' +
      ", type=" + type +
      ", createdAt=" + createdAt +
      ", updatedAt=" + updatedAt +
      '}';
  }
}
