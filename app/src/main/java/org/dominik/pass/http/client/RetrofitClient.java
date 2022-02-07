package org.dominik.pass.http.client;

import org.dominik.pass.http.interceptors.RequestInterceptor;
import org.dominik.pass.services.AccessService;

import java.util.concurrent.TimeUnit;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public final class RetrofitClient {
  //private static final String BASE_URL = "https://10.0.2.2/api/";
  private static final String BASE_URL = "https://192.168.0.17/api/";
  private static RetrofitClient INSTANCE;

  private Retrofit retrofit;

  private RetrofitClient() {}

  public static RetrofitClient getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new RetrofitClient();
      INSTANCE.setRetrofit();
    }

    return INSTANCE;
  }

  public Retrofit getRetrofit() {
    return retrofit;
  }

  public <T> T createService(Class<T> service) {
    return retrofit.create(service);
  }

  private void setRetrofit() {
    retrofit = new Retrofit.Builder()
      .baseUrl(BASE_URL)
      .client(createClient())
      .addConverterFactory(JacksonConverterFactory.create())
      .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
      .build();
  }

  private static OkHttpClient createClient() {
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
    RequestInterceptor requestInterceptor = new RequestInterceptor(AccessService.getInstance());

    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

    return new OkHttpClient.Builder()
      .callTimeout(2L, TimeUnit.MINUTES)
      .connectTimeout(20, TimeUnit.SECONDS)
      .readTimeout(30, TimeUnit.SECONDS)
      .writeTimeout(30, TimeUnit.SECONDS)
      .addInterceptor(requestInterceptor)
      .addInterceptor(loggingInterceptor)
      .build();
  }
}
