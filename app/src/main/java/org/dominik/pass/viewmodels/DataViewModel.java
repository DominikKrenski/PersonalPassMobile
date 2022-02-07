package org.dominik.pass.viewmodels;

import androidx.lifecycle.ViewModel;

import org.dominik.pass.http.repositories.PassRepository;
import org.dominik.pass.services.EncryptionService;

public final class DataViewModel extends ViewModel {
  private static final String TAG = "DATA_VIEW_MODEL";

  private PassRepository passRepository;
  private EncryptionService encryptionService;

  public void init(PassRepository passRepository, EncryptionService encryptionService) {
    this.passRepository = passRepository;
    this.encryptionService = encryptionService;
  }

  /*public void prepareAccessData(String accessTokenHEX, String refreshTokenHEX, String derivationKeyHEX, String privateKeyHEX) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
    // decrypt private key
    byte[] privateKey = PrivateStore.getInstance().decrypt(privateKeyHEX);

    // decrypt derivation key
    byte[] derivationKey = encryptionService.decryptDerivationKey(derivationKeyHEX, privateKey);

    // decrypt access token
    String accessToken = encryptionService.decryptData(accessTokenHEX, derivationKey);

    // decrypt refresh token
    String refreshToken = encryptionService.decryptData(refreshTokenHEX, derivationKey);

    AccessDataRaw dataRaw = new AccessDataRaw(derivationKey, privateKey, accessToken, refreshToken);
    Log.d(TAG, dataRaw.toString());

    accessData.setValue(dataRaw);
  }*/
}
