package io.l0neman.pluginintest;

import android.app.Application;
import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import io.l0neman.pluginlib.Core;
import io.l0neman.pluginlib.support.PLLogger;
import io.l0neman.pluginlib.util.concurrent.EasyAsync;
import io.l0neman.pluginlib.util.file.AEasyDir;
import io.l0neman.pluginlib.util.file.EasyFile;
import io.l0neman.pluginlib.util.io.IOUtils;

public class PITApp extends Application {

  private static final String TAG = PITApp.class.getSimpleName();

  @Override protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);

  }

  @Override public void onCreate() {
    super.onCreate();

  }


}
