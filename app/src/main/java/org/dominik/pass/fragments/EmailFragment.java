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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
      //TODO implement method to open new activity
    });

    return view;
  }

  private void getAuthInfo(String email) {
    Call<AuthDTO> call = passService.getAuthInfo(new EmailDTO(email.toLowerCase(Locale.ROOT)));
    call.enqueue(new Callback<AuthDTO>() {
      @Override
      public void onResponse(Call<AuthDTO> call, Response<AuthDTO> response) {
        if (response.isSuccessful()) {
          if (response.body() != null) {
            // salt has been found, it means that account already exists
            Log.d(TAG, response.body().toString());
            emailInputLayout.setError(view.getResources().getString(R.string.email_in_use));
          }
        } else {
          if (response.code() == 404) {
            // salt has not been found, it means that given email can be used
            Bundle args = new Bundle();
            args.putString(EMAIL, email);

            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.setReorderingAllowed(true);
            transaction.addToBackStack(null);
            transaction.replace(R.id.signup_fragment_container, PasswordFragment.class, args);
            transaction.commit();
          } else {
            ApiError apiError = ErrorConverter.getInstance().parseError(response);
            Log.e(TAG, apiError.toString());
            Toast.makeText(view.getContext(), view.getContext().getResources().getString(R.string.generic_error), Toast.LENGTH_LONG).show();
          }
        }
      }

      @Override
      public void onFailure(Call<AuthDTO> call, Throwable error) {
        if (error instanceof SocketTimeoutException) {
          Log.e(TAG, Arrays.toString(error.getStackTrace()));
          Toast.makeText(view.getContext(), view.getContext().getResources().getString(R.string.connection_timeout), Toast.LENGTH_LONG).show();
        } else if (error instanceof IOException) {
          Log.e(TAG, Arrays.toString(error.getStackTrace()));
          Toast.makeText(view.getContext(), view.getContext().getResources().getString(R.string.read_timeout), Toast.LENGTH_LONG).show();
        } else {
          if (call.isCanceled()) {
            Log.e(TAG, Arrays.toString(error.getStackTrace()));
            Toast.makeText(view.getContext(), view.getContext().getResources().getString(R.string.request_cancelled), Toast.LENGTH_LONG).show();
          } else {
            Log.e(TAG, Arrays.toString(error.getStackTrace()));
            Toast.makeText(view.getContext(), view.getContext().getResources().getString(R.string.generic_error), Toast.LENGTH_LONG).show();
          }
        }
      }
    });
  }
}