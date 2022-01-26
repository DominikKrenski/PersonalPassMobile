package org.dominik.pass.http.service;

import org.dominik.pass.http.dto.AuthDTO;
import org.dominik.pass.http.dto.EmailDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PassService {

  @POST("auth/salt")
  Call<AuthDTO> getAuthInfo(@Body EmailDTO emailDTO);

}
