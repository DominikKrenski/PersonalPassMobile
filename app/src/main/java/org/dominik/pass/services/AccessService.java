package org.dominik.pass.services;

import android.content.Context;

import org.dominik.pass.models.AccessData;
import org.dominik.pass.utils.SharedPrefs;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.Subject;

public final class AccessService {
  private static AccessService INSTANCE;

  private BehaviorSubject<AccessData> subject = BehaviorSubject.create();

  private AccessService() {}

  public static AccessService getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new AccessService();
    }
    return INSTANCE;
  }

  public void saveAccessData(Context context, AccessData accessData) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
    SharedPrefs sharedPrefs = SharedPrefs.getInstance();
    EncryptionService encryptionService = EncryptionService.getInstance();

    /*String encryptedDerivationKey = encryptionService.encryptDerivationKey(accessData.getDerivationKey(), accessData.getKeyHEX());
    String encryptedAccessToken = encryptionService.encryptData(accessData.getAccessToken(), accessData.getDerivationKey());
    String encryptedRefreshToken = encryptionService.encryptData(accessData.getRefreshToken(), accessData.getDerivationKey());

    sharedPrefs.writeString(context, "derivation_key", encryptedDerivationKey);
    sharedPrefs.writeString(context, "access_token", encryptedAccessToken);
    sharedPrefs.writeString(context, "refresh_token", encryptedRefreshToken);

    subject.onNext(accessData);*/
  }

  public Subject<AccessData> getData() {
    return subject;
  }
}
