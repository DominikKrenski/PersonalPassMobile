package org.dominik.pass.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.dominik.pass.R;
import org.dominik.pass.http.dto.RegistrationDataDTO;
import org.dominik.pass.utils.EncryptionService;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class FinishFragment extends Fragment {
  private static final String TAG = "FINISH_FRAGMENT";
  private static final String REGISTRATION_DATA = "registration_data";

  private RegistrationDataDTO registrationDTO;

  public FinishFragment() { }

  public static FinishFragment newInstance(RegistrationDataDTO data) {
    FinishFragment fragment = new FinishFragment();
    Bundle args = new Bundle();
    args.putSerializable(REGISTRATION_DATA, data);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments() != null) {
      registrationDTO = (RegistrationDataDTO) getArguments().getSerializable(REGISTRATION_DATA);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    Log.d(TAG, registrationDTO.toString());

    View view = inflater.inflate(R.layout.fragment_finish, container, false);

    EncryptionService encService = EncryptionService.getInstance();

    CompletableFuture
      .supplyAsync(encService::generateSalt)
      .thenApply(salt -> {
        try {
          registrationDTO.setSalt(encService.convertByteArrayToHex(salt).toLowerCase(Locale.ROOT));
          return encService.generateDerivationKey(registrationDTO.getPassword(), salt);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
          e.printStackTrace();
          return null;
        }
      })
      .thenApply(derivationKey -> {
        try {
          return encService.doubleHashDerivationKey(derivationKey);

        } catch (NoSuchAlgorithmException e) {
          e.printStackTrace();
          return null;
        }
      })
      .thenAccept(hash -> {
        registrationDTO.setPassword(encService.convertByteArrayToHex(hash).toLowerCase(Locale.ROOT));
        Log.d(TAG, registrationDTO.toString());
      });

    return view;

    //return inflater.inflate(R.layout.fragment_finish, container, false);
  }
}