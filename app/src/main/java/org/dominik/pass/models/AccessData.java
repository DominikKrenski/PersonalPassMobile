package org.dominik.pass.models;

public final class AccessData {
  String derivationKey;
  String privateKey;
  String accessToken;
  String refreshToken;

  public String getDerivationKey() {
    return derivationKey;
  }

  public void setDerivationKey(String derivationKey) {
    this.derivationKey = derivationKey;
  }

  public String getPrivateKey() {
    return privateKey;
  }

  public void setPrivateKey(String privateKey) {
    this.privateKey = privateKey;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  @Override
  public String toString() {
    return "AccessData{" +
      "derivationKey='" + derivationKey + '\'' +
      ", privateKey='" + privateKey + '\'' +
      ", accessToken='" + accessToken + '\'' +
      ", refreshToken='" + refreshToken + '\'' +
      '}';
  }
}
