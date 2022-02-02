package org.dominik.pass.http.dto;

import java.io.Serializable;

public final class RegistrationDataDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  private String email;
  private String password;
  private String salt;
  private String reminder;

  public RegistrationDataDTO() {}

  public RegistrationDataDTO(String email, String password, String salt, String reminder) {
    this.email = email;
    this.password = password;
    this.salt = salt;
    this.reminder = reminder;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public String getSalt() {
    return salt;
  }

  public String getReminder() {
    return reminder;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public void setReminder(String reminder) {
    this.reminder = reminder;
  }

  @Override
  public String toString() {
    return "RegistrationDataDTO{" +
      "email='" + email + '\'' +
      ", password='" + password + '\'' +
      ", salt='" + salt + '\'' +
      ", reminder='" + reminder + '\'' +
      '}';
  }
}
