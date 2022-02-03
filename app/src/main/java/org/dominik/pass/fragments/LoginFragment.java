package org.dominik.pass.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.dominik.pass.R;
import org.dominik.pass.errors.ApiError;
import org.dominik.pass.http.client.ApiClient;
import org.dominik.pass.http.dto.EmailDTO;
import org.dominik.pass.http.dto.LoginDataDTO;
import org.dominik.pass.http.service.PassService;
import org.dominik.pass.http.utils.ErrorConverter;
import org.dominik.pass.models.AccessData;
import org.dominik.pass.services.AccessService;
import org.dominik.pass.utils.EncryptionService;
import org.dominik.pass.utils.StringUtils;
import org.dominik.pass.utils.Validator;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class LoginFragment extends Fragment {
  private static final String TAG = "LOGIN_FRAGMENT";

  private TextView fragmentTitle;
  private TextInputLayout emailLayout;
  private TextInputLayout passwordLayout;
  private TextInputEditText emailInput;
  private TextInputEditText passwordInput;
  private MaterialButton loginButton;
  private MaterialButton registerButton;
  private View view;

  public LoginFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    view = inflater.inflate(R.layout.fragment_login, container, false);

    fragmentTitle = view.findViewById(R.id.login_title);
    fragmentTitle.setText(StringUtils.getInstance().prepareAppName(view.getContext(), view.getResources().getString(R.string.app_name)));

    emailLayout = view.findViewById(R.id.login_email_input_layout);
    passwordLayout = view.findViewById(R.id.login_password_input_layout);
    emailInput = view.findViewById(R.id.login_email_input);
    passwordInput = view.findViewById(R.id.login_password_input);
    loginButton = view.findViewById(R.id.login_button);
    registerButton = view.findViewById(R.id.register_button);

    registerButton.setOnClickListener(v -> {
      FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
      transaction.setReorderingAllowed(true);
      transaction.addToBackStack(null);
      transaction.replace(R.id.auth_fragment_container, EmailFragment.class, null);
      transaction.commit();
    });

    loginButton.setOnClickListener(v -> {
      boolean emailValid = performEmailValidation(emailInput.getText());
      boolean passwordValid = performPasswordValidation(passwordInput.getText());

      if (!(emailValid && passwordValid))
        return;

      login(Objects.requireNonNull(emailInput.getText()).toString(), Objects.requireNonNull(passwordInput.getText()).toString());
    });


    return view;
  }

  private boolean performEmailValidation(final CharSequence email) {
    Validator validator = Validator.getInstance();

    // check if email is filled in
    if (validator.notBlank(email)) {
      emailLayout.setError(null);
    } else {
      emailLayout.setError(view.getResources().getString(R.string.email_required_msg));
      return false;
    }

    // check if email has valid format
    if (validator.email(email)) {
      emailLayout.setError(null);
    } else {
      emailLayout.setError(view.getResources().getString(R.string.email_pattern_msg));
      return false;
    }

    return true;
  }

  private boolean performPasswordValidation(final CharSequence password) {
    boolean valid = Validator.getInstance().notBlank(password);

    if (valid)
      passwordLayout.setError(null);
    else
      passwordLayout.setError(view.getResources().getString(R.string.password_required_msg));

    return valid;
  }

  private void login(String email, String password) {
    EncryptionService encService = EncryptionService.getInstance();
    AccessService accessService = AccessService.getInstance();
    PassService passService = ApiClient.getInstance().create(PassService.class);
    AccessData accessData = new AccessData();

    passService
      .getAuthInfo(new EmailDTO(email))
      .flatMap(res -> {
        byte[] derivationKey = encService.regenerateDerivationKey(password, res.getSalt());
        accessData.setDerivationKey(derivationKey);
        byte[] derivationKeyHash = encService.doubleHashDerivationKey(derivationKey);
        return passService.signin(new LoginDataDTO(email, encService.convertByteArrayToHex(derivationKeyHash)));
      })
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(res -> {
          Log.d(TAG, res.toString());
        },
        err -> {
          if (err instanceof HttpException) {
            ApiError apiError = ErrorConverter.getInstance().parseError(Objects.requireNonNull(((HttpException) err).response()));
            Log.e(TAG, apiError.toString());
            Toast.makeText(view.getContext(), apiError.getMessage(), Toast.LENGTH_LONG).show();
          } else if (err instanceof SocketTimeoutException) {
            Log.e(TAG, Arrays.toString(err.getStackTrace()));
            Toast.makeText(view.getContext(), view.getResources().getString(R.string.connection_timeout), Toast.LENGTH_LONG).show();
          } else if (err instanceof IOException) {
            Log.e(TAG, Arrays.toString(err.getStackTrace()));
            Toast.makeText(view.getContext(), view.getResources().getString(R.string.read_timeout), Toast.LENGTH_LONG).show();
          } else {
            Log.e(TAG, Arrays.toString(err.getStackTrace()));
            Toast.makeText(view.getContext(), view.getResources().getString(R.string.generic_error), Toast.LENGTH_LONG).show();
          }
        },
        () -> Log.d(TAG, "Request completed")
      );
  }
}