package org.dominik.pass.models.wrappers;

import org.dominik.pass.http.enums.DataType;
import org.dominik.pass.models.entries.NoteEntry;

import java.time.Instant;
import java.util.UUID;

public final class NoteData extends BaseData {
  private NoteEntry noteEntry;

  public static Builder builder() {
    return new Builder();
  }

  public NoteEntry getNoteEntry() {
    return noteEntry;
  }

  public void setNoteEntry(NoteEntry noteEntry) {
    this.noteEntry = noteEntry;
  }

  @Override
  public String toString() {
    return "NoteData{" +
      "noteEntry=" + noteEntry +
      '}';
  }

  public static final class Builder {
    private UUID publicId;
    private DataType type;
    private Instant createdAt;
    private Instant updatedAt;
    private NoteEntry noteEntry;

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

    public Builder setNoteEntry(NoteEntry noteEntry) {
      this.noteEntry = noteEntry;
      return this;
    }

    public NoteData build() {
      NoteData noteData = new NoteData();
      noteData.setPublicId(this.publicId);
      noteData.setType(this.type);
      noteData.setCreatedAt(this.createdAt);
      noteData.setUpdatedAt(this.updatedAt);
      noteData.setNoteEntry(this.noteEntry);

      return noteData;
    }
  }
}
