package org.dominik.pass.models.wrappers;

import org.dominik.pass.http.enums.DataType;
import org.dominik.pass.models.entries.PasswordEntry;

import java.time.Instant;
import java.util.UUID;

public final class PasswordData extends BaseData {
  private PasswordEntry passwordEntry;

  public static Builder builder() {
    return new Builder();
  }

  public PasswordEntry getPasswordEntry() {
    return passwordEntry;
  }

  public void setPasswordEntry(PasswordEntry passwordEntry) {
    this.passwordEntry = passwordEntry;
  }

  @Override
  public String toString() {
    return "PasswordData{" +
      "passwordEntry=" + passwordEntry +
      '}';
  }

  public static final class Builder {
    private UUID publicId;
    private DataType type;
    private Instant createdAt;
    private Instant updatedAt;
    private PasswordEntry passwordEntry;

    public Builder setPublicId(UUID publicId) {
      this.publicId = publicId;
      return this;
    }

    public Builder setType(DataType type) {
      this.type = type;
      return this;
    }

    public Builder setCreatedAt(Instant createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public Builder setUpdatedAt(Instant updatedAt) {
      this.updatedAt = updatedAt;
      return this;
    }

    public Builder setPasswordEntry(PasswordEntry passwordEntry) {
      this.passwordEntry = passwordEntry;
      return this;
    }

    public PasswordData build() {
      PasswordData passwordData = new PasswordData();
      passwordData.setPublicId(this.publicId);
      passwordData.setType(this.type);
      passwordData.setCreatedAt(this.createdAt);
      passwordData.setUpdatedAt(this.updatedAt);
      passwordData.passwordEntry = this.passwordEntry;

      return passwordData;
    }
  }
}
