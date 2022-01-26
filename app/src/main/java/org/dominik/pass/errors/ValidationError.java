package org.dominik.pass.errors;

import java.util.List;

public final class ValidationError implements SubError{
  private final String field;
  private final Object rejectedValue;
  private final List<String> validationMessages;

  public ValidationError(String field, Object rejectedValue, List<String> validationMessages) {
    this.field = field;
    this.rejectedValue = rejectedValue;
    this.validationMessages = validationMessages;
  }

  public String getField() {
    return field;
  }

  public Object getRejectedValue() {
    return rejectedValue;
  }

  public List<String> getValidationMessages() {
    return validationMessages;
  }

  @Override
  public String toString() {
    return "ValidationError{" +
      "field='" + field + '\'' +
      ", rejectedValue=" + rejectedValue +
      ", validationMessages=" + validationMessages +
      '}';
  }
}
