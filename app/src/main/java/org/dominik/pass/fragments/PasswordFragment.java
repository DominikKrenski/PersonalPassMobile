package org.dominik.pass.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.dominik.pass.R;
import org.dominik.pass.http.dto.RegistrationDataDTO;
import org.dominik.pass.utils.Validator;

import java.io.Serializable;
import java.util.Objects;

public class PasswordFragment extends Fragment {
  private static final String TAG = "PASSWORD_FRAGMENT";
  private static final String EMAIL = "email_address";
  private static final String REGISTRATION_DATA = "registration_data";

  private String email;

  private TextView subtitle;
  private LinearLayout checkboxLayout;
  private TextInputLayout passwordConfirmLayout;
  private TextInputLayout passwordHintLayout;

  private TextInputEditText passwordInput;
  private TextInputEditText passwordConfirmInput;
  private TextInputEditText passwordHintInput;

  private MaterialCheckBox lengthCheckbox;
  private MaterialCheckBox numberCheckbox;
  private MaterialCheckBox lowercaseCheckbox;
  private MaterialCheckBox uppercaseCheckbox;
  private MaterialCheckBox emailCheckbox;

  private View view;

  public PasswordFragment() {
    // Required empty public constructor
  }

  public static PasswordFragment newInstance(String email) {
    PasswordFragment fragment = new PasswordFragment();
    Bundle bundle = new Bundle();
    bundle.putString(EMAIL, email);
    fragment.setArguments(bundle);

    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments() != null) {
      email = getArguments().getString(EMAIL);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    Log.d(TAG, "Email address: " + email);

    view = inflater.inflate(R.layout.fragment_password, container, false);

    subtitle = view.findViewById(R.id.password_subtitle);
    passwordInput = view.findViewById(R.id.password_input);
    passwordConfirmInput = view.findViewById(R.id.password_confirm_input);
    passwordHintInput = view.findViewById(R.id.password_hint_input);

    checkboxLayout = view.findViewById(R.id.checkbox_layout);
    passwordConfirmLayout = view.findViewById(R.id.password_confirm_input_layout);
    passwordHintLayout = view.findViewById(R.id.password_hint_input_layout);


    lengthCheckbox = view.findViewById(R.id.length_checkbox);
    numberCheckbox = view.findViewById(R.id.number_checkbox);
    lowercaseCheckbox = view.findViewById(R.id.lowercase_checkbox);
    uppercaseCheckbox = view.findViewById(R.id.uppercase_checkbox);
    emailCheckbox = view.findViewById(R.id.email_checkbox);

    MaterialButton passwordButton = view.findViewById(R.id.password_button);

    passwordButton.setOnClickListener(this::onClick);


    passwordInput.setOnFocusChangeListener((view, hasFocus) -> {
      if (view.isFocused()) {
        subtitle.setVisibility(View.GONE);
        checkboxLayout.setVisibility(View.VISIBLE);
      }
    });

    passwordInput.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        performPasswordValidation(charSequence);
      }

      @Override
      public void afterTextChanged(Editable editable) { }
    });

    return view;
  }

  private boolean performPasswordValidation(final CharSequence password) {
    // check if password is at least 12 characters long
    lengthCheckbox.setChecked(Validator.getInstance().minLength(password, 12));

    // check if password contains at least one number
    numberCheckbox.setChecked(Validator.getInstance().oneNumber(password));

    // check if password contains at least one lowercase letter
    lowercaseCheckbox.setChecked(Validator.getInstance().oneLowercase(password));

    // check if password contains at least one uppercase letter
    uppercaseCheckbox.setChecked(Validator.getInstance().oneUppercase(password));

    // check if password not contains email (left part)
    emailCheckbox.setChecked(Validator.getInstance().notEmail(email, password));

    return lengthCheckbox.isChecked() && numberCheckbox.isChecked() && lowercaseCheckbox.isChecked() && uppercaseCheckbox.isChecked() && emailCheckbox.isChecked();
  }

  private boolean performPasswordConfirmValidation(final CharSequence password, final CharSequence confirmation) {
    boolean valid = Validator.getInstance().equalValues(password, confirmation);

    if (valid)
      passwordConfirmLayout.setError(null);
    else
      passwordConfirmLayout.setError(view.getResources().getString(R.string.passwords_not_equal));

    return valid;
  }

  private boolean performHintValidation(final CharSequence hint) {
    boolean valid = Validator.getInstance().maxLength(hint, 255);

    if (valid)
      passwordHintLayout.setError(null);
    else
      passwordHintLayout.setError(view.getResources().getString(R.string.password_hint_max_length));

    return valid;
  }

  private void onClick(View v) {
    boolean passwordValid = performPasswordValidation(passwordInput.getText());
    boolean passwordConfirmValid = performPasswordConfirmValidation(passwordInput.getText(), passwordConfirmInput.getText());
    boolean hintValid = performHintValidation(passwordHintInput.getText());

    if (!passwordValid || !passwordConfirmValid || !hintValid)
      return;

    // go to the finish screen
    RegistrationDataDTO dto = new RegistrationDataDTO(
      email,
      Objects.requireNonNull(passwordInput.getText()).toString(),
      null,
      Objects.requireNonNull(passwordHintInput.getText()).toString()
    );

    Bundle args = new Bundle();
    args.putSerializable(REGISTRATION_DATA, (Serializable) dto);

    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
    transaction.setReorderingAllowed(true);
    transaction.addToBackStack(null);
    transaction.replace(R.id.signup_fragment_container, FinishFragment.class, args);
    transaction.commit();
  }
}