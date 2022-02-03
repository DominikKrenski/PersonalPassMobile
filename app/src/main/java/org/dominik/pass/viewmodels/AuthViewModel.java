package org.dominik.pass.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.dominik.pass.enums.ErrorType;
import org.dominik.pass.errors.ApiError;
import org.dominik.pass.errors.ErrorWrapper;
import org.dominik.pass.http.dto.AuthDTO;
import org.dominik.pass.http.dto.RegistrationDataDTO;
import org.dominik.pass.http.repositories.PassRepository;
import org.dominik.pass.http.utils.ErrorConverter;
import org.dominik.pass.models.AccessData;
import org.dominik.pass.services.EncryptionService;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public final class AuthViewModel extends ViewModel {
  private static final String TAG = "AUTH_VIEW_MODEL";

  private final MutableLiveData<AccessData> signinAccessData = new MutableLiveData<>();
  private final MutableLiveData<ErrorWrapper> signinError = new MutableLiveData<>();

  private final MutableLiveData<AccessData> signupAccessData = new MutableLiveData<>();
  private final MutableLiveData<ErrorWrapper> signupError = new MutableLiveData<>();

  private final MutableLiveData<AuthDTO> saltAccessData = new MutableLiveData<>();
  private final MutableLiveData<ErrorWrapper> saltError = new MutableLiveData<>();

  private PassRepository passRepository;
  private EncryptionService encryptionService;

  private Disposable disposable;
  private Disposable disposable1;
  private Disposable disposable2;

  public void init(PassRepository passRepository, EncryptionService encryptionService) {
    this.passRepository = passRepository;
    this.encryptionService = encryptionService;
  }

  public LiveData<AccessData> getSigninAccessData() {
    return signinAccessData;
  }

  public LiveData<ErrorWrapper> getSigninError() {
    return signinError;
  }

  public LiveData<AccessData> getSignupAccessData() {
    return signupAccessData;
  }

  public LiveData<ErrorWrapper> getSignupError() {
    return signupError;
  }

  public LiveData<AuthDTO> getSalt() {
    return saltAccessData;
  }

  public LiveData<ErrorWrapper> getSaltError() {
    return saltError;
  }

  public void getSalt(String email) {
    disposable2 = passRepository
      .getAuthInfo(email)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(res -> {
          saltAccessData.setValue(res);
          saltError.setValue(null);
        },
        err -> {
          Log.e(TAG, Arrays.toString(err.getStackTrace()));
          ErrorWrapper errorWrapper = new ErrorWrapper();
          if (err instanceof HttpException) {
            ApiError apiError = ErrorConverter.getInstance().parseError(Objects.requireNonNull(((HttpException) err).response()));
            errorWrapper.setApiError(apiError);
          } else if (err instanceof SocketTimeoutException) {
            errorWrapper.setErrorType(ErrorType.SOCKET_ERROR);
          } else if (err instanceof IOException) {
            errorWrapper.setErrorType(ErrorType.IO_ERROR);
          } else {
            errorWrapper.setErrorType(ErrorType.GENERIC_ERROR);
          }

          saltAccessData.setValue(null);
          saltError.setValue(errorWrapper);
        });
  }

  public void register(String email, String password, String reminder) {
    RegistrationDataDTO registrationData = new RegistrationDataDTO();
    AccessData loginData = new AccessData();

    registrationData.setEmail(email);

    disposable1 = Observable
      .just(encryptionService.generateRandomBytes(16))
      .flatMap(salt -> {
        registrationData.setSalt(encryptionService.convertByteArrayToHex(salt));
        return Observable.just(encryptionService.generateDerivationKey(password, salt));
      })
      .flatMap(derivationKey -> Observable.just(encryptionService.doubleHashDerivationKey(derivationKey)))
      .map(encryptionService::convertByteArrayToHex)
      .flatMap(keyHex -> {
        registrationData.setPassword(keyHex);

        if (reminder == null || reminder.equals(""))
          registrationData.setReminder(null);
        else
          registrationData.setReminder(reminder);

        return passRepository.signup(registrationData);
      })
      .flatMap(res -> passRepository.signin(registrationData.getEmail(), registrationData.getPassword()))
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(res -> {
          // encrypt all data and pass it to the fragment
          byte[] derivationKey = encryptionService.regenerateDerivationKey(password, registrationData.getSalt());
          loginData.setAccessToken(encryptionService.encryptData(res.getAccessToken(), derivationKey));
          loginData.setRefreshToken(encryptionService.encryptData(res.getRefreshToken(), derivationKey));
          loginData.setKeyHEX(res.getKey());
          loginData.setDerivationKey(encryptionService.encryptDerivationKey(derivationKey, res.getKey()).getBytes(StandardCharsets.UTF_8));

          signupAccessData.setValue(loginData);
          signupError.setValue(null);
        },
        err -> {
          Log.e(TAG, Arrays.toString(err.getStackTrace()));
          ErrorWrapper errorWrapper = new ErrorWrapper();
          if (err instanceof HttpException) {
            ApiError apiError = ErrorConverter.getInstance().parseError(Objects.requireNonNull(((HttpException) err).response()));
            errorWrapper.setApiError(apiError);
          } else if (err instanceof SocketTimeoutException) {
            errorWrapper.setErrorType(ErrorType.SOCKET_ERROR);
          } else if (err instanceof IOException) {
            errorWrapper.setErrorType(ErrorType.IO_ERROR);
          } else {
            errorWrapper.setErrorType(ErrorType.GENERIC_ERROR);
          }

          signupAccessData.setValue(null);
          signupError.setValue(errorWrapper);
        });
  }

  public void login(String email, String password) {
    AccessData data = new AccessData();

    disposable = passRepository
      .getAuthInfo(email)
      .flatMap(res -> {
        byte[] derivationKey = encryptionService.regenerateDerivationKey(password, res.getSalt());
        data.setDerivationKey(derivationKey);
        byte[] derivationKeyHash = encryptionService.doubleHashDerivationKey(derivationKey);
        return passRepository.signin(email, encryptionService.convertByteArrayToHex(derivationKeyHash));
      })
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(res -> {
          // encrypt all data and pass it to the fragment
          data.setAccessToken(encryptionService.encryptData(res.getAccessToken(), data.getDerivationKey()));
          data.setRefreshToken(encryptionService.encryptData(res.getRefreshToken(), data.getDerivationKey()));
          data.setKeyHEX(res.getKey());
          data.setDerivationKey(encryptionService.encryptDerivationKey(data.getDerivationKey(), data.getKeyHEX()).getBytes(StandardCharsets.UTF_8));

          signinAccessData.setValue(data);
          signinError.setValue(null);
        },
        err -> {
          ErrorWrapper errorWrapper = new ErrorWrapper();
          if (err instanceof HttpException) {
            ApiError apiError = ErrorConverter.getInstance().parseError(Objects.requireNonNull(((HttpException) err).response()));
            errorWrapper.setApiError(apiError);
          } else if (err instanceof SocketTimeoutException) {
            errorWrapper.setErrorType(ErrorType.SOCKET_ERROR);
          } else if (err instanceof IOException) {
            errorWrapper.setErrorType(ErrorType.IO_ERROR);
          } else {
            errorWrapper.setErrorType(ErrorType.GENERIC_ERROR);
          }

          signinAccessData.setValue(null);
          signinError.setValue(errorWrapper);
        });
  }

  @Override
  protected void onCleared() {
    disposable.dispose();
    disposable1.dispose();
    disposable2.dispose();
    super.onCleared();
  }
}
