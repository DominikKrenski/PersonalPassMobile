package org.dominik.pass.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import org.dominik.pass.R;
import org.dominik.pass.fragments.EmailFragment;
import org.dominik.pass.fragments.LoginFragment;

public class AuthActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_auth);

    Intent intent = getIntent();
    String fragmentType = intent.getStringExtra(LaunchActivity.FRAGMENT_TYPE);

    if (savedInstanceState == null) {
      if (fragmentType.equals("signup")) {
        getSupportFragmentManager()
          .beginTransaction()
          .setReorderingAllowed(true)
          .add(R.id.auth_fragment_container, EmailFragment.class, null)
          .commit();
      } else {
        getSupportFragmentManager()
          .beginTransaction()
          .setReorderingAllowed(true)
          .add(R.id.auth_fragment_container, LoginFragment.class, null)
          .commit();
      }
    }
  }
}