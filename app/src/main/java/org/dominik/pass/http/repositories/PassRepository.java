package org.dominik.pass.http.repositories;

import org.dominik.pass.http.dto.AuthDTO;
import org.dominik.pass.http.dto.DataDTO;
import org.dominik.pass.http.dto.EmailDTO;
import org.dominik.pass.http.dto.LoginDataDTO;
import org.dominik.pass.http.dto.RegistrationDataDTO;
import org.dominik.pass.http.service.PassService;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public final class PassRepository {
  private static PassRepository INSTANCE;

  private PassService passService;

  public static PassRepository getInstance() {
    if (INSTANCE == null)
      INSTANCE = new PassRepository();

    return INSTANCE;
  }

  public void setPassService(PassService passService) {
    this.passService = passService;
  }

  public Observable<AuthDTO> getAuthInfo(String email) {
    return passService.getAuthInfo(new EmailDTO(email));
  }

  public Observable<AuthDTO> signin(String email, String password) {
    return passService.signin(new LoginDataDTO(email, password));
  }

  public Observable<AuthDTO> signup(RegistrationDataDTO data) {
    return passService.signup(data);
  }

  public Observable<List<DataDTO>> getAllData() { return passService.getAllData(); }
}
