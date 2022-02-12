package org.dominik.pass.models.wrappers;

import org.dominik.pass.http.enums.DataType;
import org.dominik.pass.models.entries.SiteEntry;

import java.time.Instant;
import java.util.UUID;

public final class SiteData extends BaseData {
  private SiteEntry siteEntry;

  public static Builder builder() {
    return new Builder();
  }

  public SiteEntry getSiteEntry() {
    return siteEntry;
  }

  public void setSiteEntry(SiteEntry siteEntry) {
    this.siteEntry = siteEntry;
  }

  @Override
  public String toString() {
    return "SiteData{" +
      "siteEntry=" + siteEntry +
      '}';
  }

  public static final class Builder {
    private UUID publicId;
    private DataType type;
    private Instant createdAt;
    private Instant updatedAt;
    private SiteEntry siteEntry;

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

    public Builder setSiteEntry(SiteEntry siteEntry) {
      this.siteEntry = siteEntry;
      return this;
    }

    public SiteData build() {
      SiteData siteData = new SiteData();
      siteData.setPublicId(this.publicId);
      siteData.setType(this.type);
      siteData.setCreatedAt(this.createdAt);
      siteData.setUpdatedAt(this.updatedAt);
      siteData.siteEntry = this.siteEntry;

      return siteData;
    }
  }
}
