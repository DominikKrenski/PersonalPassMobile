package org.dominik.pass.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.dominik.pass.R;
import org.dominik.pass.fragments.EmailFragment;

public class SignupActivity extends AppCompatActivity {

  public SignupActivity() {
    super(R.layout.activity_signup);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (savedInstanceState == null) {
      getSupportFragmentManager()
        .beginTransaction()
        .setReorderingAllowed(true)
        .add(R.id.signup_fragment_container, EmailFragment.class, null)
        .commit();
    }
  }
}