package io.l0neman.plugintarget;

import android.app.Application;
import android.content.Context;

import io.l0neman.plugintarget.util.TALogger;

/**
 * Created by l0neman on 2019/07/07.
 */
public class TargetApp extends Application {

  private static final String TAG = TargetApp.class.getSimpleName();

  @Override protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    TALogger.d(TAG, "attachBaseContext");
  }

  @Override public void onCreate() {
    super.onCreate();
    TALogger.d(TAG, "onCreate");
  }
}
