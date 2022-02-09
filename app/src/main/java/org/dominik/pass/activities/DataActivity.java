package org.dominik.pass.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.dominik.pass.R;
import org.dominik.pass.http.client.RetrofitClient;
import org.dominik.pass.http.repositories.PassRepository;
import org.dominik.pass.http.service.PassService;
import org.dominik.pass.services.AccessService;
import org.dominik.pass.services.EncryptionService;
import org.dominik.pass.utils.SharedPrefs;
import org.dominik.pass.viewmodels.DataViewModel;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class DataActivity extends AppCompatActivity {
  private static final String TAG = "DATA_ACTIVITY";

  private DataViewModel dataViewModel;
  private TextView menuHeaderUser;
  private Toolbar menuToolbar;
  private NavigationView navigationView;
  private DrawerLayout menuLayout;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_data);

    // create PassRepository instance
    PassRepository passRepository = PassRepository.getInstance();
    passRepository.setPassService(RetrofitClient.getInstance().createService(PassService.class));

    dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
    dataViewModel.init(passRepository, EncryptionService.getInstance());

    // pass access data
    try {
      AccessService.getInstance().passAccessData(this);
    } catch (InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
      Toast.makeText(this, getResources().getString(R.string.missing_access_data), Toast.LENGTH_LONG).show();
    }

    menuToolbar = findViewById(R.id.menu_toolbar);
    setSupportActionBar(menuToolbar);

    menuLayout = findViewById(R.id.menu_layout);
    navigationView = findViewById(R.id.navigation_view);

    ActionBarDrawerToggle actionToggle = new ActionBarDrawerToggle(
      this,
      menuLayout,
      menuToolbar,
      R.string.openNavDrawer,
      R.string.closeNavDrawer
    );

    menuLayout.addDrawerListener(actionToggle);
    actionToggle.syncState();

    String currentUser = SharedPrefs.getInstance().readString(this, "current_user", null);

    View navHeader = navigationView.getHeaderView(0);
    menuHeaderUser = navHeader.findViewById(R.id.nav_header_user);
    menuHeaderUser.setText(currentUser);

    navigationView.setNavigationItemSelectedListener(item -> {
      if (item.getTitle().equals(getString(R.string.addresses_item))) {
        item.setChecked(true);
        Log.d(TAG, "Addresses clicked");
      }

      return true;
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    dataViewModel.getAllData();
  }

  @Override
  public void onBackPressed() {
    if (menuLayout.isDrawerOpen(GravityCompat.START))
      menuLayout.closeDrawer(GravityCompat.START);
    else
      super.onBackPressed();
  }
}