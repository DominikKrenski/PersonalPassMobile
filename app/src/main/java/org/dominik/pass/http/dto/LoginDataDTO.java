package org.dominik.pass.http.dto;

import androidx.annotation.NonNull;

public final class LoginDataDTO {
  private String email;
  private String password;

  public LoginDataDTO(@NonNull String email, @NonNull String password) {
    this.email = email;
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "LoginDataDTO{" +
      "email='" + email + '\'' +
      ", password='" + password + '\'' +
      '}';
  }
}
