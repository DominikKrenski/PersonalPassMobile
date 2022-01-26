package org.dominik.pass.http.dto;

import java.util.UUID;

public final class AuthDTO {
  private UUID publicId;
  private String salt;
  private String key;
  private String accessToken;
  private String refreshToken;

  public UUID getPublicId() {
    return publicId;
  }

  public String getSalt() {
    return salt;
  }

  public String getKey() {
    return key;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  @Override
  public String toString() {
    return "AuthDTO{" +
      "publicId=" + publicId +
      ", salt='" + salt + '\'' +
      ", key='" + key + '\'' +
      ", accessToken='" + accessToken + '\'' +
      ", refreshToken='" + refreshToken + '\'' +
      '}';
  }
}
