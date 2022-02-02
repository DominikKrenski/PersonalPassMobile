package org.dominik.pass.models;

import java.util.Arrays;

public final class AccessData {
  byte[] derivationKey;
  String keyHEX;
  String accessToken;
  String refreshToken;

  public byte[] getDerivationKey() {
    return derivationKey;
  }

  public void setDerivationKey(byte[] derivationKey) {
    this.derivationKey = derivationKey;
  }

  public String getKeyHEX() {
    return keyHEX;
  }

  public void setKeyHEX(String keyHEX) {
    this.keyHEX = keyHEX;
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
      "derivationKey=" + Arrays.toString(derivationKey) +
      ", keyHEX='" + keyHEX + '\'' +
      ", accessToken='" + accessToken + '\'' +
      ", refreshToken='" + refreshToken + '\'' +
      '}';
  }
}
