package org.dominik.pass.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.dominik.pass.http.dto.DataDTO;
import org.dominik.pass.http.repositories.PassRepository;
import org.dominik.pass.models.AccessDataRaw;
import org.dominik.pass.models.wrappers.AllData;
import org.dominik.pass.models.wrappers.BaseData;
import org.dominik.pass.services.AccessService;
import org.dominik.pass.services.EncryptionService;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public final class DataViewModel extends ViewModel {
  private static final String TAG = "DATA_VIEW_MODEL";

  private PassRepository passRepository;
  private EncryptionService encryptionService;
  private AccessService accessService;
  private AccessDataRaw accessData;

  private Disposable allDataDisposable;
  private Disposable accessDataDisposable;

  private final MutableLiveData<List<DataDTO>> allData = new MutableLiveData<>();
  private final MutableLiveData<AllData> selectedData = new MutableLiveData<>();
  private final MutableLiveData<BaseData> currentData = new MutableLiveData<>();

  public void init(PassRepository passRepository, EncryptionService encryptionService, AccessService accessService) {
    this.passRepository = passRepository;
    this.encryptionService = encryptionService;
    this.accessService = accessService;
    accessDataDisposable = accessService.getAccessData().subscribe(data -> accessData = data);
  }

  public void fetchAllData() {
    allDataDisposable = passRepository
      .getAllData()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(res -> {
        res.forEach(el -> {
          try {
            el.setEntry(encryptionService.decryptData(el.getEntry(), accessData.getDerivationKey()));
          } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
          }
        });

        allData.setValue(res);
      }, err-> Log.d(TAG, Arrays.toString(err.getStackTrace())));
  }

  public LiveData<List<DataDTO>> getAllData() { return allData; }

  public void passSelectedData(AllData selected) {
    selectedData.setValue(selected);
  }

  public LiveData<AllData> getSelectedData() {
    return selectedData;
  }

  public void passCurrentData(BaseData data) {
    currentData.setValue(data);
  }

  public LiveData<BaseData> getCurrentData() {
    return currentData;
  }

  @Override
  protected void onCleared() {
    allDataDisposable.dispose();
    accessDataDisposable.dispose();
    super.onCleared();
  }
}
