package org.dominik.pass.utils;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import org.dominik.pass.R;

public final class StringUtils {
  private static StringUtils INSTANCE;

  private StringUtils() {}

  public SpannableString prepareAppName(Context ctx, String text) {
    int personalIdx = text.indexOf("Personal");
    int passIdx = text.indexOf("Pass");

    SpannableString spanString = new SpannableString(text);

    if (personalIdx == -1 || passIdx == -1)
      return spanString;

    spanString.setSpan(new ForegroundColorSpan(ctx.getResources().getColor(R.color.grey, null)), personalIdx, personalIdx + 8, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
    spanString.setSpan(new ForegroundColorSpan(ctx.getResources().getColor(R.color.red, null)), passIdx, passIdx + 4, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
    return spanString;
  }

  public static StringUtils getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new StringUtils();
    }

    return INSTANCE;
  }
}
