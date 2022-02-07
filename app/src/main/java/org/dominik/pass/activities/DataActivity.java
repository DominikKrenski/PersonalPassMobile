package org.dominik.pass.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Toast;

import org.dominik.pass.R;
import org.dominik.pass.http.client.RetrofitClient;
import org.dominik.pass.http.repositories.PassRepository;
import org.dominik.pass.http.service.PassService;
import org.dominik.pass.services.AccessService;
import org.dominik.pass.services.EncryptionService;
import org.dominik.pass.viewmodels.DataViewModel;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class DataActivity extends AppCompatActivity {
  private DataViewModel dataViewModel;

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
  }

}