package org.dominik.pass.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

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
import org.dominik.pass.activities.DataActivity;
import org.dominik.pass.enums.ErrorType;
import org.dominik.pass.utils.SharedPrefs;
import org.dominik.pass.utils.StringUtils;
import org.dominik.pass.utils.Validator;
import org.dominik.pass.viewmodels.AuthViewModel;

import java.util.Objects;

public class LoginFragment extends Fragment {
  private static final String TAG = "LOGIN_FRAGMENT";
  private AuthViewModel authViewModel;

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

      authViewModel.login(Objects.requireNonNull(emailInput.getText()).toString(), Objects.requireNonNull(passwordInput.getText()).toString());
    });


    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

    authViewModel
      .getSigninAccessData()
      .observe(getViewLifecycleOwner(), data -> {
        if (data != null) {
          SharedPrefs.getInstance().writeString(view.getContext(), "current_user", Objects.requireNonNull(emailInput.getText()).toString());
          SharedPrefs.getInstance().writeString(view.getContext(), "encrypted_access_token", data.getAccessToken());
          SharedPrefs.getInstance().writeString(view.getContext(), "encrypted_refresh_token", data.getRefreshToken());
          SharedPrefs.getInstance().writeString(view.getContext(), "encrypted_derivation_key", data.getDerivationKey());
          SharedPrefs.getInstance().writeString(view.getContext(), "encrypted_private_key", data.getPrivateKey());

          // go to the DataActivity
          Intent intent = new Intent(view.getContext(), DataActivity.class);
          startActivity(intent);
        }
      });

    authViewModel
      .getSigninError()
      .observe(getViewLifecycleOwner(), err -> {
        if (err == null)
          return;

        if (err.getApiError() != null) {
          Toast.makeText(view.getContext(), err.getApiError().getMessage(), Toast.LENGTH_LONG).show();
          return;
        }

        if (err.getErrorType() == ErrorType.SOCKET_ERROR) {
          Toast.makeText(view.getContext(), view.getResources().getString(R.string.connection_timeout), Toast.LENGTH_LONG).show();
        } else if (err.getErrorType() == ErrorType.IO_ERROR) {
          Toast.makeText(view.getContext(), view.getResources().getString(R.string.read_timeout), Toast.LENGTH_LONG).show();
        } else {
          Toast.makeText(view.getContext(), view.getResources().getString(R.string.generic_error), Toast.LENGTH_LONG).show();
        }
      });
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
}