package org.dominik.pass.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import org.dominik.pass.R;

import org.dominik.pass.enums.ErrorType;
import org.dominik.pass.http.dto.RegistrationDataDTO;
import org.dominik.pass.utils.SharedPrefs;
import org.dominik.pass.viewmodels.AuthViewModel;

import java.util.Arrays;

public class FinishFragment extends Fragment {
  private static final String TAG = "FINISH_FRAGMENT";
  private static final String REGISTRATION_DATA = "registration_data";

  private AuthViewModel authViewModel;

  private MaterialButton finishButton;

  private RegistrationDataDTO registrationData;

  public FinishFragment() {
  }

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
      registrationData = (RegistrationDataDTO) getArguments().getSerializable(REGISTRATION_DATA);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_finish, container, false);

    finishButton = view.findViewById(R.id.finish_button);

    finishButton.setOnClickListener(v -> {
      authViewModel.register(registrationData.getEmail(), registrationData.getPassword(), registrationData.getReminder());
    });

    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

    authViewModel
      .getSignupAccessData()
      .observe(getViewLifecycleOwner(), data -> {
        if (data != null) {
          SharedPrefs.getInstance().writeString(view.getContext(), "encrypted_access_token", data.getAccessToken());
          SharedPrefs.getInstance().writeString(view.getContext(), "encrypted_refresh_token", data.getRefreshToken());
          SharedPrefs.getInstance().writeString(view.getContext(), "encrypted_derivation_key", new String(data.getDerivationKey()));
          SharedPrefs.getInstance().writeString(view.getContext(), "encrypted_private_key", data.getKeyHEX());
        }
      });

    authViewModel
      .getSignupError()
      .observe(getViewLifecycleOwner(), err -> {
        if (err == null)
          return;

        if (err.getApiError() != null) {
          Toast.makeText(view.getContext(), err.getApiError().getMessage(), Toast.LENGTH_LONG).show();
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
}