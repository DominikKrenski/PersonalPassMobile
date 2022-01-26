package org.dominik.pass.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
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
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailFragment extends Fragment {
  private static final String TAG = "EMAIL_FRAGMENT";

  private View view;
  private TextInputLayout inputLayout;
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
    inputLayout = view.findViewById(R.id.email_input);

    passService = ApiClient.getInstance().create(PassService.class);

    Validator validator = Validator.getInstance();

    if (savedInstanceState != null)
      Objects.requireNonNull(inputLayout.getEditText()).setText(savedInstanceState.getString("email"));

    signupBtn.setOnClickListener(v -> {
      String email = Objects.requireNonNull(inputLayout.getEditText()).getText().toString().trim();

      // check if email is not empty
      if (!validator.required(email)) {
        inputLayout.setError(view.getResources().getString(R.string.email_required_msg));
        return;
      }

      // check if email is valid
      if (!validator.email(email)) {
        inputLayout.setError(view.getResources().getString(R.string.email_pattern_msg));
        return;
      }

      getAuthInfo(email);

    });

    signinBtn.setOnClickListener(v -> {
      //TODO implement method to open new activity
    });

    return view;
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);

    String email = Objects.requireNonNull(inputLayout.getEditText()).getText().toString();
    outState.putString("email", email);
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
            Toast.makeText(view.getContext(), view.getResources().getString(R.string.email_in_use), Toast.LENGTH_LONG).show();
          }
        } else {
          if (response.code() == 404) {
            // salt has not been found, it means that given email can be used
            //TODO go to the next screen
          } else {
            ApiError apiError = ErrorConverter.getInstance().parseError(response);
            Log.d(TAG, apiError.toString());
          }
        }
      }

      @Override
      public void onFailure(Call<AuthDTO> call, Throwable error) {
        if (error instanceof SocketTimeoutException) {
          Log.d(TAG, "CONNECTION TIMEOUT");
        } else if (error instanceof IOException) {
          Log.d(TAG, "TIMEOUT");
        } else {
          if (call.isCanceled()) {
            Log.d(TAG, "REQUEST WAS CANCELLED BY THE USER");
          } else {
            Log.d(TAG, "GENERIC ERROR");
          }
        }
      }
    });
  }
}