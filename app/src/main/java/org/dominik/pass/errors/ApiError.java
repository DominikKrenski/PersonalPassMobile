package org.dominik.pass.errors;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.dominik.pass.utils.ApiInstantDeserializer;

import java.time.Instant;
import java.util.List;

public final class ApiError {
  private String status;
  private Instant timestamp;
  private String message;
  private List<? extends SubError> errors;

  public ApiError() {}

  public String getStatus() {
    return status;
  }

  @JsonDeserialize(using = ApiInstantDeserializer.class)
  public Instant getTimestamp() {
    return timestamp;
  }

  public String getMessage() {
    return message;
  }

  public List<? extends SubError> getErrors() {
    return errors;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setTimestamp(Instant timestamp) {
    this.timestamp = timestamp;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void setErrors(List<? extends SubError> errors) {
    this.errors = errors;
  }

  @Override
  public String toString() {
    return "ApiError{" +
      "status='" + status + '\'' +
      ", timestamp=" + timestamp +
      ", message='" + message + '\'' +
      ", errors=" + errors +
      '}';
  }
}
