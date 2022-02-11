package org.dominik.pass.models.wrappers;

import org.dominik.pass.http.enums.DataType;

import java.time.Instant;
import java.util.UUID;

public abstract class BaseData {
  private UUID publicId;
  private DataType type;
  private Instant createdAt;
  private Instant updatedAt;

  public UUID getPublicId() {
    return publicId;
  }

  public void setPublicId(UUID publicId) {
    this.publicId = publicId;
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
}
