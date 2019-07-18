package io.l0neman.pluginintest;

import android.app.Application;
import android.content.Context;

import io.l0neman.pluginlib.Core;

public class PITApp extends Application {

  private static final String TAG = PITApp.class.getSimpleName();

  @Override protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    Core.getInstance().initEnv(this);
  }

  @Override public void onCreate() {
    super.onCreate();

  }


}
