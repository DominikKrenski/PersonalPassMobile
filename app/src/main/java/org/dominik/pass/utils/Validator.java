package org.dominik.pass.utils;

import java.util.Locale;
import java.util.regex.Pattern;

public final class Validator {
  private static final String EMAIL_PATTERN = "^(?=.{1,254}$)(?=.{1,64}@)[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
  private static final String ONE_NUMBER_PATTERN = "^.*\\d.*$";
  private static final String ONE_LOWERCASE_PATTERN = "^.*[a-zęóąśłżźćń].*$";
  private static final String ONE_UPPERCASE_PATTERN = "^.*[A-ZĘÓĄŚŁŻŹĆŃ].*$";
  private static Validator INSTANCE;

  private Validator() {}

  public boolean notBlank(CharSequence input) {
    if (input == null)
      return false;

    return input.toString().trim().length() > 0;
  }

  public boolean email(CharSequence input) {
    if (input == null)
      return false;

    return Pattern.matches(EMAIL_PATTERN, input);
  }

  public boolean oneNumber(CharSequence input) {
    if (input == null)
      return false;

    return Pattern.matches(ONE_NUMBER_PATTERN, input);
  }

  public boolean oneLowercase(CharSequence input) {
    if (input == null)
      return false;

    return Pattern.matches(ONE_LOWERCASE_PATTERN, input);
  }

  public boolean oneUppercase(CharSequence input) {
    if (input == null)
      return false;

    return Pattern.matches(ONE_UPPERCASE_PATTERN, input);
  }

  public boolean minLength(final CharSequence input, final int minLength) {
    if (input == null)
      return false;

    return input.length() >= minLength;
  }

  public boolean maxLength(CharSequence input, int maxLength) {
    if (input == null)
      return false;

    return input.length() <= maxLength;
  }

  public boolean notEmail(CharSequence email, CharSequence input) {
    if (email == null || input == null || email.toString().length() == 0 || input.toString().length() == 0)
      return false;

    String[] emailParts = email.toString().split("@");

    return !input.toString().toLowerCase(Locale.ROOT).contains(emailParts[0].toLowerCase(Locale.ROOT));
  }

  public boolean equalValues(CharSequence value1, CharSequence value2) {
    if (value1 == null || value2 == null)
      return false;

    return value1.toString().equals(value2.toString());
  }

  public static Validator getInstance() {
    if (INSTANCE == null)
      INSTANCE = new Validator();

    return INSTANCE;
  }
}
