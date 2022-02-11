package org.dominik.pass.models.entries;

public final class PasswordEntry {
  private String entryTitle;
  private String url;
  private String username;
  private String password;
  private String notes;

  public static Builder builder() {
    return new Builder();
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

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  @Override
  public String toString() {
    return "PasswordEntry{" +
      "entryTitle='" + entryTitle + '\'' +
      ", url='" + url + '\'' +
      ", username='" + username + '\'' +
      ", password='" + password + '\'' +
      ", notes='" + notes + '\'' +
      '}';
  }

  public static final class Builder {
    private String entryTitle;
    private String url;
    private String username;
    private String password;
    private String notes;

    public Builder setEntryTitle(String entryTitle) {
      this.entryTitle = entryTitle;
      return this;
    }

    public Builder setUrl(String url) {
      this.url = url;
      return this;
    }

    public Builder setUsername(String username) {
      this.username = username;
      return this;
    }

    public Builder setPassword(String password) {
      this.password = password;
      return this;
    }

    public Builder setNotes(String notes) {
      this.notes = notes;
      return this;
    }

    public PasswordEntry build() {
      PasswordEntry passwordEntry = new PasswordEntry();
      passwordEntry.entryTitle = this.entryTitle;
      passwordEntry.url = this.url;
      passwordEntry.username = this.username;
      passwordEntry.password = this.password;
      passwordEntry.notes = this.notes;

      return passwordEntry;
    }
  }
}
