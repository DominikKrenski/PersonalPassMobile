package org.dominik.pass.http.service;

import org.dominik.pass.http.dto.AuthDTO;
import org.dominik.pass.http.dto.DataDTO;
import org.dominik.pass.http.dto.EmailDTO;
import org.dominik.pass.http.dto.LoginDataDTO;
import org.dominik.pass.http.dto.RegistrationDataDTO;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PassService {

  @POST("auth/salt")
  Observable<AuthDTO> getAuthInfo(@Body EmailDTO emailDTO);

  @POST("auth/signup")
  Observable<AuthDTO> signup(@Body RegistrationDataDTO data);

  @POST("auth/signin")
  Observable<AuthDTO> signin(@Body LoginDataDTO data);

  @GET("data/all")
  Observable<List<DataDTO>> getAllData();
}
