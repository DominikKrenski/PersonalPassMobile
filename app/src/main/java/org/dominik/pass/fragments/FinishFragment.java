package org.dominik.pass.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import org.dominik.pass.R;

import org.dominik.pass.errors.ApiError;
import org.dominik.pass.http.client.ApiClient;
import org.dominik.pass.http.dto.LoginDataDTO;
import org.dominik.pass.http.dto.RegistrationDataDTO;
import org.dominik.pass.http.service.PassService;
import org.dominik.pass.http.utils.ErrorConverter;
import org.dominik.pass.models.AccessData;
import org.dominik.pass.services.AccessService;
import org.dominik.pass.utils.EncryptionService;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class FinishFragment extends Fragment {
  private static final String TAG = "FINISH_FRAGMENT";
  private static final String REGISTRATION_DATA = "registration_data";

  private MaterialButton finishButton;

  private RegistrationDataDTO registrationData;

  public FinishFragment() {
  }

  public static FinishFragment newInstance(RegistrationDataDTO data) {
    FinishFragment fragment = new FinishFragment();
    Bundle args = new Bundle();
    args.putSerializable(REGISTRATION_DATA, data);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments() != null) {
      registrationData = (RegistrationDataDTO) getArguments().getSerializable(REGISTRATION_DATA);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_finish, container, false);

    finishButton = view.findViewById(R.id.finish_button);

    finishButton.setOnClickListener(v -> {
      EncryptionService encService = EncryptionService.getInstance();
      AccessService accessService = AccessService.getInstance();
      PassService passService = ApiClient.getInstance().create(PassService.class);
      AccessData accessData = new AccessData();

      Observable
        .just(encService.generateRandomBytes(16))
        .flatMap(salt -> {
          registrationData.setSalt(encService.convertByteArrayToHex(salt));
          return Observable.just(encService.generateDerivationKey(registrationData.getPassword(), salt));
        })
        .flatMap(derivationKey -> {
          accessData.setDerivationKey(derivationKey);
          return Observable.just(encService.doubleHashDerivationKey(derivationKey));
        })
        .map(encService::convertByteArrayToHex)
        .flatMap(hexKey -> {
          registrationData.setPassword(hexKey);

          if (registrationData.getReminder().equals(""))
            registrationData.setReminder(null);

          return passService.signup(registrationData);
        })
        .flatMap(res -> {
          LoginDataDTO loginData = new LoginDataDTO(registrationData.getEmail(), registrationData.getPassword());
          return passService.signin(loginData);
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(res -> {
            accessData.setKeyHEX(res.getKey());
            accessData.setAccessToken(res.getAccessToken());
            accessData.setRefreshToken(res.getRefreshToken());
            accessService.saveAccessData(view.getContext(), accessData);
          },
          err -> {
            if (err instanceof HttpException) {
              ApiError apiError = ErrorConverter.getInstance().parseError(Objects.requireNonNull(((HttpException) err).response()));
              Log.e(TAG, apiError.toString());
            } else if (err instanceof SocketTimeoutException) {
              Log.e(TAG, Arrays.toString(err.getStackTrace()));
              Toast.makeText(v.getContext(), view.getResources().getString(R.string.connection_timeout), Toast.LENGTH_LONG).show();
            } else if (err instanceof IOException) {
              Log.e(TAG, Arrays.toString(err.getStackTrace()));
              Toast.makeText(view.getContext(), view.getResources().getString(R.string.read_timeout), Toast.LENGTH_LONG).show();
            } else {
              Log.e(TAG, Arrays.toString(err.getStackTrace()));
              Toast.makeText(view.getContext(), view.getResources().getString(R.string.generic_error), Toast.LENGTH_LONG).show();
            }
          },
          () -> Log.d(TAG, "Request finished"));
    });

    return view;
  }
}