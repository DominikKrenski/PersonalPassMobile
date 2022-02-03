package org.dominik.pass.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import org.dominik.pass.R;
import org.dominik.pass.fragments.EmailFragment;
import org.dominik.pass.fragments.LoginFragment;
import org.dominik.pass.http.client.RetrofitClient;
import org.dominik.pass.http.repositories.PassRepository;
import org.dominik.pass.http.service.PassService;
import org.dominik.pass.services.EncryptionService;
import org.dominik.pass.viewmodels.AuthViewModel;

public class AuthActivity extends AppCompatActivity {
  private AuthViewModel authViewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_auth);

    // create PassRepository instance
    PassRepository passRepository = PassRepository.getInstance();
    passRepository.setPassService(RetrofitClient.getInstance().createService(PassService.class));

    authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    authViewModel.init(passRepository, EncryptionService.getInstance());


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