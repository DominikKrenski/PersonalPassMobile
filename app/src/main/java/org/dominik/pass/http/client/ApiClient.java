package org.dominik.pass.http.client;

import org.dominik.pass.http.interceptors.RequestInterceptor;

import java.util.concurrent.TimeUnit;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public final class ApiClient {
  //private static final String BASE_URL = "https://10.0.2.2:443/api/";
  private static final String BASE_URL = "https://192.168.0.17/api/";
  private static Retrofit INSTANCE;

  private ApiClient() {}

  public static Retrofit getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(createClient())
        .addConverterFactory(JacksonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build();
    }

    return INSTANCE;
  }

  private static OkHttpClient createClient() {
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

    RequestInterceptor requestInterceptor = new RequestInterceptor();

    return  new OkHttpClient.Builder()
      .callTimeout(2L, TimeUnit.MINUTES)
      .connectTimeout(20, TimeUnit.SECONDS)
      .readTimeout(30, TimeUnit.SECONDS)
      .writeTimeout(30, TimeUnit.SECONDS)
      //.addInterceptor(requestInterceptor)
      .addInterceptor(loggingInterceptor)
      .build();
  }
}
