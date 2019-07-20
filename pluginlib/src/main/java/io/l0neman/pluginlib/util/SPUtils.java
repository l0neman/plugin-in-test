package io.l0neman.pluginlib.util;

import android.content.Context;
import android.content.SharedPreferences;

public final class SPUtils {
  public static SharedPreferences get(Context ctx, String file) {
    return ctx.getSharedPreferences(file, Context.MODE_PRIVATE);
  }

  public static void apply(Context ctx, String file, Object[] kvPairs) {
    SharedPreferences sp = ctx.getSharedPreferences(file, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sp.edit();
    for (int i = 0; i < kvPairs.length; i += 2) {
      String key = (String) kvPairs[i];
      Object val = kvPairs[i + 1];

      applyValueToEditor(editor, key, val);
    }
    editor.apply();
  }

  public static void apply(Context ctx, String file, String key, String val) {
    SharedPreferences sp = ctx.getSharedPreferences(file, Context.MODE_PRIVATE);
    final SharedPreferences.Editor editor = sp.edit();

    applyValueToEditor(editor, key, val);
    editor.apply();
  }

  private static void applyValueToEditor(SharedPreferences.Editor editor, String key, Object val) {
    if (val instanceof String) {
      editor.putString(key, (String) val);
    } else if (val instanceof Boolean) {
      editor.putBoolean(key, (boolean) val);
    } else if (val instanceof Integer) {
      editor.putInt(key, (int) val);
    } else if (val instanceof Long) {
      editor.putLong(key, (long) val);
    } else if (val instanceof Float) {
      editor.putFloat(key, (float) val);
    }
  }
}