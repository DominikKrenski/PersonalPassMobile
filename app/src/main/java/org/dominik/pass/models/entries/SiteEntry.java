package org.dominik.pass.models.entries;

public final class SiteEntry {
  private String entryTitle;
  private String url;

  public SiteEntry() {}

  public SiteEntry(String entryTitle, String url) {
    this.entryTitle = entryTitle;
    this.url = url;
  }

  public String getEntryTitle() {
    return entryTitle;
  }

  public void setEntryTitle(String entryTitle) {
    this.entryTitle = entryTitle;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public String toString() {
    return "SiteEntry{" +
      "entryTitle='" + entryTitle + '\'' +
      ", url='" + url + '\'' +
      '}';
  }
}
