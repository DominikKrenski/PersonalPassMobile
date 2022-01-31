package org.dominik.pass.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class EncryptionService {
  private static final int ITERATIONS = 100100;
  private static final int DERIVATION_KEY_LENGTH = 256;

  private static EncryptionService INSTANCE;

  private EncryptionService() {}

  public String convertByteArrayToHex(byte[] arr) {
    StringBuilder builder = new StringBuilder();

    for (byte i : arr)
      builder.append(String.format("%02X", i));

    return builder.toString();
  }

  public byte[] convertHexToByteArray(String hex) {
    byte[] arr = new byte[hex.length() / 2];

    for (int i = 0; i < arr.length; i++) {
      int index = i * 2;
      int value = Integer.parseInt(hex.substring(index, index + 2), 16);
      arr[i] = (byte) value;
    }

    return arr;
  }

  public byte[] generateSalt() {
    byte[] bytes = new byte[16];
    new SecureRandom().nextBytes(bytes);

    return bytes;
  }


  public byte[] doubleHashDerivationKey(byte[] derivationKey) throws NoSuchAlgorithmException {
    MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
    byte[] firstHash = messageDigest.digest(derivationKey);

    return messageDigest.digest(firstHash);
  }

  public byte[] generateDerivationKey(final String password, final byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
    KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, DERIVATION_KEY_LENGTH);
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

    return factory.generateSecret(keySpec).getEncoded();
  }

  public static EncryptionService getInstance() {
    if (INSTANCE == null)
      INSTANCE = new EncryptionService();

    return INSTANCE;
  }
}
