package org.dominik.pass.models;

import java.util.Arrays;

public final class AccessDataRaw {
  private byte[] derivationKey;
  private byte[] privateKey;
  private String accessToken;
  private String refreshToken;

  public byte[] getDerivationKey() {
    return derivationKey;
  }

  public void setDerivationKey(byte[] derivationKey) {
    this.derivationKey = derivationKey;
  }

  public byte[] getPrivateKey() {
    return privateKey;
  }

  public void setPrivateKey(byte[] privateKey) {
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
    return "AccessDataRaw{" +
      "derivationKey=" + Arrays.toString(derivationKey) +
      ", privateKey=" + Arrays.toString(privateKey) +
      ", accessToken='" + accessToken + '\'' +
      ", refreshToken='" + refreshToken + '\'' +
      '}';
  }
}
