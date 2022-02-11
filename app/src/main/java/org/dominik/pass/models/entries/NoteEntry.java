package org.dominik.pass.models.entries;

public final class NoteEntry {
  private String entryTitle;
  private String message;

  public NoteEntry() {}

  public NoteEntry(String entryTitle, String message) {
    this.entryTitle = entryTitle;
    this.message = message;
  }

  public String getEntryTitle() {
    return entryTitle;
  }

  public void setEntryTitle(String entryTitle) {
    this.entryTitle = entryTitle;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return "NoteEntry{" +
      "entryTitle='" + entryTitle + '\'' +
      ", message='" + message + '\'' +
      '}';
  }
}
