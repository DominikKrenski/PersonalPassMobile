package org.dominik.pass.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.dominik.pass.errors.ErrorWrapper;
import org.dominik.pass.http.dto.DataDTO;
import org.dominik.pass.http.repositories.PassRepository;
import org.dominik.pass.services.EncryptionService;

import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public final class DataViewModel extends ViewModel {
  private static final String TAG = "DATA_VIEW_MODEL";

  private PassRepository passRepository;
  private EncryptionService encryptionService;

  private Disposable allDataDisposable;

  private final MutableLiveData<List<DataDTO>> allData = new MutableLiveData<>();

  public void init(PassRepository passRepository, EncryptionService encryptionService) {
    this.passRepository = passRepository;
    this.encryptionService = encryptionService;
  }

  public void getAllData() {
    allDataDisposable = passRepository
      .getAllData()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(res -> {
        allData.setValue(res);
        Log.d(TAG, res.toString());
      },
        err -> Log.d(TAG, Arrays.toString(err.getStackTrace())));
  }

  @Override
  protected void onCleared() {
    allDataDisposable.dispose();
    super.onCleared();
  }
}
