package org.dominik.pass.http.dto;

public final class EmailDTO {
  private final String email;

  public EmailDTO(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }
}
