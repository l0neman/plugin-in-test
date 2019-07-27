package io.l0neman.pluginintest;

import android.app.Application;
import android.content.Context;

import io.l0neman.pluginlib.Core;
import io.l0neman.pluginlib.support.PLLogger;

public class PITApp extends Application {

  private static final String TAG = PITApp.class.getSimpleName();

  @Override protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    PLLogger.i(TAG, "attachBaseContext");

    Core.getInstance().initEnv(this);
  }

  @Override public void onCreate() {
    PLLogger.i(TAG, "onCreate");
    super.onCreate();

    Core.getInstance().preload();
  }

}
