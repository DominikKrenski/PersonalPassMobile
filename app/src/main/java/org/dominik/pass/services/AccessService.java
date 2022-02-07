package org.dominik.pass.services;

import android.content.Context;

import org.dominik.pass.models.AccessDataRaw;
import org.dominik.pass.utils.SharedPrefs;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.reactivex.rxjava3.subjects.BehaviorSubject;

public final class AccessService {
  private static final String DERIVATION_KEY = "encrypted_derivation_key";
  private static final String PRIVATE_KEY = "encrypted_private_key";
  private static final String ACCESS_TOKEN = "encrypted_access_token";
  private static final String REFRESH_TOKEN = "encrypted_refresh_token";

  private static AccessService INSTANCE;

  private BehaviorSubject<AccessDataRaw> subject = BehaviorSubject.create();

  private AccessService() {}

  public static AccessService getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new AccessService();
    }
    return INSTANCE;
  }

  public void passAccessData(Context ctx) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
    SharedPrefs sharedPrefs = SharedPrefs.getInstance();
    EncryptionService encryptionService = EncryptionService.getInstance();
    PrivateStore privateStore = PrivateStore.getInstance();

    String encryptedDerivationKey = sharedPrefs.readString(ctx, DERIVATION_KEY, null);
    String encryptedPrivateKey = sharedPrefs.readString(ctx, PRIVATE_KEY, null);
    String encryptedAccessToken = sharedPrefs.readString(ctx, ACCESS_TOKEN, null);
    String encryptedRefreshToken = sharedPrefs.readString(ctx, REFRESH_TOKEN, null);

    // decrypt private key
    byte[] privateKey = privateStore.decrypt(encryptedPrivateKey);

    // decrypt derivation key
    byte[] derivationKey = encryptionService.decryptDerivationKey(encryptedDerivationKey, privateKey);

    // decrypt access token
    String accessToken = encryptionService.decryptData(encryptedAccessToken, derivationKey);

    // decrypt refresh token
    String refreshToken = encryptionService.decryptData(encryptedRefreshToken, derivationKey);

    AccessDataRaw accessDataRaw = new AccessDataRaw(derivationKey, privateKey, accessToken, refreshToken);

    subject.onNext(accessDataRaw);
  }

  public BehaviorSubject<AccessDataRaw> getAccessData() {
    return subject;
  }
}
