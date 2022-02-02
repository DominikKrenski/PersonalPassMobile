package org.dominik.pass.utils;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public final class EncryptionService {
  private static final int ITERATIONS = 100100;
  private static final int DERIVATION_KEY_LENGTH = 256;
  private static final int IV_LENGTH = 12;
  private static final int TAG_LENGTH = 16;
  private static final String ENCRYPTION_ALGORITHM = "AES/GCM/NoPadding";

  private static EncryptionService INSTANCE;

  private EncryptionService() {}

  public String convertByteArrayToHex(byte[] arr) {
    StringBuilder builder = new StringBuilder();

    for (byte i : arr)
      builder.append(String.format("%02X", i));

    return builder.toString().toLowerCase(Locale.ROOT);
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

  public byte[] generateRandomBytes(int length) {
    byte[] bytes = new byte[length];
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

  public byte[] regenerateDerivationKey(final String password, final String saltHEX) throws NoSuchAlgorithmException, InvalidKeySpecException {
    byte[] salt = convertHexToByteArray(saltHEX);

    KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, DERIVATION_KEY_LENGTH);
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

    return factory.generateSecret(keySpec).getEncoded();
  }

  public String encryptData(String data, byte[] derivationKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    // generate initialization vector
    byte[] iv = generateRandomBytes(IV_LENGTH);

    // create GCMParameterSpec
    GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(TAG_LENGTH * 8, iv);

    // convert derivationKey to SecretKey
    SecretKey secretKey = new SecretKeySpec(derivationKey, "AES");

    // create cipher instance for AES-GCM
    Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);

    // initialize Cipher for ENCRYPT_MODE
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);

    // perform encryption
    byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

    // convert iv to HEX
    String ivHEX = convertByteArrayToHex(iv);

    // convert encrypted data to HEX
    String encryptedDataHEX = convertByteArrayToHex(encryptedData);

    return ivHEX + "." + encryptedDataHEX;
  }

  public String decryptData(String encryptedDataHEX, byte[] derivationKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    String[] tmp = encryptedDataHEX.split("\\.");

    // convert iv to byte[]
    byte[] iv = convertHexToByteArray(tmp[0]);

    // convert encrypted data HEX to byte[]
    byte[] encryptedData = convertHexToByteArray(tmp[1]);

    // convert derivation key in SecretKey
    SecretKey secretKey = new SecretKeySpec(derivationKey, "AES");

    // create GCMParameterSpec instance
    GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(TAG_LENGTH * 8, iv);

    // initialize Cipher for ENCRYPT_MODE
    Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);

    // decrypt data
    byte[] decryptedData = cipher.doFinal(encryptedData);

    return new String(decryptedData);
  }

  public String encryptDerivationKey(byte[] derivationKey, String encryptionKeyHEX) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    // convert encryptionKeyHEX to byte[]
    byte[] encryptionKey = convertHexToByteArray(encryptionKeyHEX);

    // generate initialization vector
    byte[] iv = generateRandomBytes(IV_LENGTH);

    // create GCMParameterSpec instance
    GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(TAG_LENGTH * 8, iv);

    // convert encryptionKey to SecretKey
    SecretKey secretKey = new SecretKeySpec(encryptionKey, "AES");

    // create Cipher instance for AES-GCM
    Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);

    // initialize Cipher for ENCRYPT_MODE
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);

    // perform encryption
    byte[] encryptedDerivationKey = cipher.doFinal(derivationKey);

    // convert encrypted derivation key to HEX
    String encryptedDerivationKeyHEX = convertByteArrayToHex(encryptedDerivationKey);

    // convert iv to HEX
    String ivHEX = convertByteArrayToHex(iv);

    // concatenate and return encrypted derivation key in format ivHEX.derivationKeyHEX
    return ivHEX + "." + encryptedDerivationKeyHEX;
  }

  public byte[] decryptDerivationKey(String encryptedDerivationKeyHEX, String encryptionKeyHEX) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    // split derivationKeyHex into two parts -> 1. ivHEX 2. derivationKeyHEX
    String[] tmp = encryptedDerivationKeyHEX.split("\\.");

    byte[] iv = convertHexToByteArray(tmp[0]);
    byte[] encryptedDerivationKey = convertHexToByteArray(tmp[1]);
    byte[] enryptionKey = convertHexToByteArray(encryptionKeyHEX);

    // convert encryption key to SecretKey
    SecretKey secretKey = new SecretKeySpec(enryptionKey, "AES");

    // create GCMParameterSpec instance
    GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(TAG_LENGTH * 8, iv);

    // Initialize Cipher for DECRYPT MODE
    Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);

    // decrypt data
    return cipher.doFinal(encryptedDerivationKey);
  }

  public static EncryptionService getInstance() {
    if (INSTANCE == null)
      INSTANCE = new EncryptionService();

    return INSTANCE;
  }
}
