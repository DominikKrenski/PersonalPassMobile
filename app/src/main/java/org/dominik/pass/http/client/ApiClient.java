package org.dominik.pass.http.client;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public final class ApiClient {
  private static final String BASE_URL = "https://10.0.2.2:443/api/";
  private static Retrofit INSTANCE;

  private ApiClient() {}

  public static Retrofit getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(createClient())
        .addConverterFactory(JacksonConverterFactory.create())
        .build();
    }

    return INSTANCE;
  }

  private static OkHttpClient createClient() {
    return  new OkHttpClient.Builder()
      .callTimeout(2L, TimeUnit.MINUTES)
      .connectTimeout(20, TimeUnit.SECONDS)
      .readTimeout(30, TimeUnit.SECONDS)
      .writeTimeout(30, TimeUnit.SECONDS)
      .build();
  }
}
