package org.dominik.pass.utils;

import android.content.Context;
import android.content.SharedPreferences;

public final class SharedPrefs {
  private static final String PREFS_NAME = "org.dominik.pass.APP_DATA";

  private static SharedPrefs INSTANCE;

  private SharedPrefs() {}

  public static SharedPrefs getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new SharedPrefs();
    }

    return INSTANCE;
  }

  public void writeString(Context ctx, String entryKey, String data) {
    SharedPreferences prefs = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = prefs.edit();
    editor.putString(entryKey, data);
    editor.apply();
  }

  public String readString(Context ctx, String entryKey, String defaultValue) {
    SharedPreferences prefs = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    return prefs.getString(entryKey, defaultValue);
  }

  public void writeBoolean(Context ctx, String entryKey, boolean data) {
    SharedPreferences prefs = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = prefs.edit();
    editor.putBoolean(entryKey, data);
    editor.apply();
  }

  public boolean readBoolean(Context ctx, String entryKey, boolean defaultValue) {
    SharedPreferences prefs = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    return prefs.getBoolean(entryKey, defaultValue);
  }
}
