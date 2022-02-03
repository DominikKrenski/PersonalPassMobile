package org.dominik.pass.errors;

import org.dominik.pass.enums.ErrorType;

public final class ErrorWrapper {
  private ApiError apiError;
  private ErrorType errorType;

  public ErrorWrapper() {}

  public ErrorWrapper(ApiError apiError, ErrorType errorType) {
    this.apiError = apiError;
    this.errorType = errorType;
  }

  public ApiError getApiError() {
    return apiError;
  }

  public void setApiError(ApiError apiError) {
    this.apiError = apiError;
  }

  public ErrorType getErrorType() {
    return errorType;
  }

  public void setErrorType(ErrorType errorType) {
    this.errorType = errorType;
  }
}
