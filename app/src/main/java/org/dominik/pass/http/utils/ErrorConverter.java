package org.dominik.pass.http.utils;

import org.dominik.pass.errors.ApiError;
import org.dominik.pass.http.client.RetrofitClient;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.time.Instant;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public final class ErrorConverter {
  private static ErrorConverter INSTANCE;

  private ErrorConverter() {}

  public static ErrorConverter getInstance() {
    if (INSTANCE == null)
      INSTANCE = new ErrorConverter();

    return INSTANCE;
  }

  public ApiError parseError(Response<?> response) {
    Converter<ResponseBody, ApiError> converter = RetrofitClient.getInstance().getRetrofit().responseBodyConverter(ApiError.class, new Annotation[0]);
    ApiError apiError = null;

    try {
      assert response.errorBody() != null;
      apiError = converter.convert(response.errorBody());
    } catch (IOException ex) {
      apiError = new ApiError();
      apiError.setStatus("Internal Server Error");
      apiError.setMessage("Error could not be deserialized");
      apiError.setTimestamp(Instant.now());
    }

    return apiError;
  }
}
