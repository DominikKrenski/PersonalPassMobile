package org.dominik.pass.models.wrappers;

import org.dominik.pass.http.enums.DataType;
import org.dominik.pass.models.entries.AddressEntry;

import java.time.Instant;
import java.util.UUID;

public final class AddressData extends BaseData {
  private AddressEntry addressEntry;

  public static Builder builder() {
    return new Builder();
  }


  public AddressEntry getAddressEntry() {
    return addressEntry;
  }

  public void setAddressEntry(AddressEntry addressEntry) {
    this.addressEntry = addressEntry;
  }

  @Override
  public String toString() {
    return "AddressData{" +
      "addressEntry=" + addressEntry +
      '}';
  }

  public static final class Builder {
    private UUID publicId;
    private DataType type;
    private Instant createdAt;
    private Instant updatedAt;
    private AddressEntry addressEntry;

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

    public Builder setAddressEntry(AddressEntry addressEntry) {
      this.addressEntry = addressEntry;
      return this;
    }

    public AddressData build() {
      AddressData addressData = new AddressData();
      addressData.setPublicId(this.publicId);
      addressData.setType(this.type);
      addressData.setCreatedAt(this.createdAt);
      addressData.setUpdatedAt(this.updatedAt);
      addressData.setAddressEntry(addressEntry);

      return addressData;
    }
  }
}
