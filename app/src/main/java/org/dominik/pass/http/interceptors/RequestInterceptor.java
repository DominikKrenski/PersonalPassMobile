package org.dominik.pass.http.interceptors;

import androidx.annotation.NonNull;

import org.dominik.pass.models.AccessDataRaw;
import org.dominik.pass.services.AccessService;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public final class RequestInterceptor implements Interceptor {
  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String BEARER = "Bearer";
  private static final String REFRESH_URL = "/api/auth/refresh";
  private static final String SIGNUP_URL = "/api/auth/signup";
  private static final String SIGNIN_URL = "/api/auth/signin";
  private static final String SALT_URL = "/api/auth/salt";
  private static final String HINT_URL = "/api/accounts/hint";

  private static final String TAG = "REQUEST_INTERCEPTOR";

  private AccessDataRaw accessData;

  public RequestInterceptor(AccessService accessService) {
    accessService.getAccessData().subscribe(data -> accessData = data);
  }

  @NonNull
  @Override
  public Response intercept(@NonNull Chain chain) throws IOException {
    Request request = chain.request();
    String path = request.url().encodedPath();

    if (path.equals(REFRESH_URL)) {
      request = request.newBuilder().header(AUTHORIZATION_HEADER, BEARER + " " + accessData.getRefreshToken()).build();
      return chain.proceed(request);
    }

    if (!path.equals(SIGNUP_URL) && !path.equals(SIGNIN_URL) && !path.equals(SALT_URL) && !path.equals(HINT_URL)) {
      request = request.newBuilder().header(AUTHORIZATION_HEADER, BEARER + " " + accessData.getAccessToken()).build();
    }

    return chain.proceed(request);
  }
}
