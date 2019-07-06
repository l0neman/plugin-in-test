package io.l0neman.pluginintest;

import android.app.Application;
import android.content.Context;

import io.l0neman.pluginlib.Core;

public class PITApp extends Application {

  @Override protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    Core.appAttachBaseContext(this);
  }

  @Override public void onCreate() {
    super.onCreate();
  }
}
