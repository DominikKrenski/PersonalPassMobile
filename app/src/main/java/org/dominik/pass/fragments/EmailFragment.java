package org.dominik.pass.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.dominik.pass.R;
import org.dominik.pass.enums.ErrorType;
import org.dominik.pass.utils.Validator;
import org.dominik.pass.viewmodels.AuthViewModel;

import java.util.Objects;


public class EmailFragment extends Fragment {
  private static final String TAG = "EMAIL_FRAGMENT";
  private static final String EMAIL = "email_address";

  private AuthViewModel authViewModel;

  private View view;
  private TextInputLayout emailInputLayout;
  private TextInputEditText emailInput;

  public EmailFragment() {
  }

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

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

    authViewModel
      .getSalt()
      .observe(getViewLifecycleOwner(), res -> {
        if (res != null)
          emailInputLayout.setError(view.getResources().getString(R.string.email_in_use));
      });

    authViewModel
      .getSaltError()
      .observe(getViewLifecycleOwner(), err -> {
        if (err == null)
          return;

        if (err.getApiError() != null) {
          if (err.getApiError().getStatus().equals("Not Found")) {
            // salt has not been found, it means that account does not exist
            Bundle args = new Bundle();
            args.putString(EMAIL, Objects.requireNonNull(emailInput.getText()).toString());

            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.setReorderingAllowed(true);
            transaction.addToBackStack(null);
            transaction.replace(R.id.auth_fragment_container, PasswordFragment.class, args);
            transaction.commit();
          } else {
            Toast.makeText(view.getContext(), err.getApiError().getMessage(), Toast.LENGTH_LONG).show();
          }
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

  private void getAuthInfo(String email) {
    authViewModel.getSalt(email);
  }
}