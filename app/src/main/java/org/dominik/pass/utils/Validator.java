package org.dominik.pass.utils;

import java.util.regex.Pattern;

public final class Validator {
  private static final String EMAIL_PATTERN = "^(?=.{1,254}$)(?=.{1,64}@)[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";

  private static Validator INSTANCE;

  private Validator() {}

  public boolean required(String input) {
    return input != null && input.trim().length() != 0;
  }

  public boolean email(final String email) {
    return Pattern.matches(EMAIL_PATTERN, email);
  }

  public static Validator getInstance() {
    if (INSTANCE == null)
      INSTANCE = new Validator();

    return INSTANCE;
  }
}
