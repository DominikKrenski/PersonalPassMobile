package org.dominik.pass.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.dominik.pass.R;
import org.dominik.pass.utils.Validator;

import java.util.Objects;

public class EmailFragment extends Fragment {
  private TextInputLayout inputLayout;

  public EmailFragment() { }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_email, container, false);

    MaterialButton signupBtn = view.findViewById(R.id.email_signup_button);
    MaterialButton signinBtn = view.findViewById(R.id.email_signin_button);
    inputLayout = view.findViewById(R.id.email_input);

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


      //TODO implement method to check if email is already in use
      //TODO implement fragment swap if everything is all right

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
}