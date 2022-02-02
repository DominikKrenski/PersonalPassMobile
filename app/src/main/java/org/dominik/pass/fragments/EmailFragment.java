package org.dominik.pass.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.dominik.pass.R;
import org.dominik.pass.errors.ApiError;
import org.dominik.pass.http.client.ApiClient;
import org.dominik.pass.http.dto.AuthDTO;
import org.dominik.pass.http.dto.EmailDTO;
import org.dominik.pass.http.service.PassService;
import org.dominik.pass.http.utils.ErrorConverter;
import org.dominik.pass.utils.Validator;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class EmailFragment extends Fragment {
  private static final String TAG = "EMAIL_FRAGMENT";
  private static final String EMAIL = "email_address";

  private View view;
  private TextInputLayout emailInputLayout;
  private TextInputEditText emailInput;
  private PassService passService;

  public EmailFragment() { }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    view = inflater.inflate(R.layout.fragment_email, container, false);

    MaterialButton signupBtn = view.findViewById(R.id.email_signup_button);
    MaterialButton signinBtn = view.findViewById(R.id.email_signin_button);
    emailInputLayout = view.findViewById(R.id.email_input_layout);
    emailInput = view.findViewById(R.id.email_input);

    passService = ApiClient.getInstance().create(PassService.class);

    Validator validator = Validator.getInstance();

    signupBtn.setOnClickListener(v -> {
      String email = Objects.requireNonNull(emailInput.getText()).toString();

      // check if email is not empty
      if (!validator.notBlank(email)) {
        emailInputLayout.setError(view.getResources().getString(R.string.email_required_msg));
        return;
      }

      // check if email is valid
      if (!validator.email(email)) {
        emailInputLayout.setError(view.getResources().getString(R.string.email_pattern_msg));
        return;
      }

      getAuthInfo(email);

    });

    signinBtn.setOnClickListener(v -> {
      FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
      transaction.setReorderingAllowed(true);
      transaction.addToBackStack(null);
      transaction.replace(R.id.auth_fragment_container, LoginFragment.class, null);
      transaction.commit();
    });

    return view;
  }

  private void getAuthInfo(String email) {
    passService
      .getAuthInfo(new EmailDTO(email.toLowerCase(Locale.ROOT)))
      .subscribeOn(Schedulers.computation())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(
        response -> {
        // salt has been found, it means that account already exists
        Log.d(TAG, response.toString());
        emailInputLayout.setError(view.getResources().getString(R.string.email_in_use));
      },
        err -> {
        if (err instanceof HttpException) {
          if (((HttpException) err).code() == 404) {
            ApiError apiError = ErrorConverter.getInstance().parseError(Objects.requireNonNull(((HttpException) err).response()));
            Log.d(TAG, apiError.toString());

            // salt has not been found, it means that given email can be used
            Bundle args = new Bundle();
            args.putString(EMAIL, email);

            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.setReorderingAllowed(true);
            transaction.addToBackStack(null);
            transaction.replace(R.id.auth_fragment_container, PasswordFragment.class, args);
            transaction.commit();
          }
        } else if (err instanceof SocketTimeoutException) {
          Log.e(TAG, Arrays.toString(err.getStackTrace()));
          Toast.makeText(view.getContext(), view.getContext().getResources().getString(R.string.connection_timeout), Toast.LENGTH_LONG).show();
        } else if (err instanceof IOException) {
          Log.e(TAG, Arrays.toString(err.getStackTrace()));
          Toast.makeText(view.getContext(), view.getContext().getResources().getString(R.string.read_timeout), Toast.LENGTH_LONG).show();
        } else {
          Log.e(TAG, Arrays.toString(err.getStackTrace()));
          Toast.makeText(view.getContext(), view.getContext().getResources().getString(R.string.generic_error), Toast.LENGTH_LONG).show();
        }
        },
        () -> Log.d(TAG, "REQUEST COMPLETED"));
  }
}