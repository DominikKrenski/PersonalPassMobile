package org.dominik.pass.http.interceptors;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public final class RequestInterceptor implements Interceptor {
  private static final String TAG = "REQUEST_INTERCEPTOR";

  @NonNull
  @Override
  public Response intercept(@NonNull Chain chain) throws IOException {
    Request request = chain.request();
    RequestBody body = request.body();


    Log.d(TAG, Objects.requireNonNull(Objects.requireNonNull(request.body()).contentType()).toString());

    request = request
      .newBuilder()
      .removeHeader("Content-Type")
      .addHeader("Content-Type", "application/json")
      .method(request.method(), request.body())
      .build();

    Log.d(TAG, request.header("Content-Type"));
    return chain.proceed(request);
  }
}
