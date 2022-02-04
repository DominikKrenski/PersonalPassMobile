package org.dominik.pass.services;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public final class PrivateStore {
  private static final String TRANSFORMATION = KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_NONE;
  private static final String ANDROID_KEY_STORE = "AndroidKeyStore";
  private static final String KEY_ALIAS = "privateKey";
  private static final String SEPARATOR = ".";

  private static PrivateStore INSTANCE;

  private KeyStore keyStore;
  private SecretKey secretKey;

  private PrivateStore() {
    try {
      keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
      keyStore.load(null);

      KeyStore.SecretKeyEntry keyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(KEY_ALIAS, null);

      if (keyEntry != null) {
        secretKey = keyEntry.getSecretKey();
      } else {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE);
        KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(
          KEY_ALIAS,
          KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT
        )
          .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
          .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
          .build();

        keyGenerator.init(spec);
        secretKey = keyGenerator.generateKey();
      }
    } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException | UnrecoverableEntryException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
      e.printStackTrace();
    }
  }

  public String encrypt(byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    Cipher cipher = Cipher.getInstance(TRANSFORMATION);
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    byte[] iv = cipher.getIV();
    byte[] encryptedData = cipher.doFinal(data);

    return EncryptionService.getInstance().convertByteArrayToHex(iv) + SEPARATOR + EncryptionService.getInstance().convertByteArrayToHex(encryptedData);
  }

  public String decrypt(String dataHEX) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    String[] parts = dataHEX.split("\\.");

    byte[] iv = parts[0].getBytes(StandardCharsets.UTF_8);
    byte[] encryptedData = parts[1].getBytes(StandardCharsets.UTF_8);

    Cipher cipher = Cipher.getInstance(TRANSFORMATION);
    IvParameterSpec spec = new IvParameterSpec(iv);

    cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
    return EncryptionService.getInstance().convertByteArrayToHex(cipher.doFinal(encryptedData));
  }

  public static PrivateStore getInstance() {
    if (INSTANCE == null)
      INSTANCE = new PrivateStore();

    return INSTANCE;
  }
}
