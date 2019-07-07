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
    Core.initEnv(this);
  }

  @Override public void onCreate() {
    super.onCreate();

    releaseTargetAPK();
  }

  private void releaseTargetAPK() {
    EasyAsync.get(new EasyAsync.Executor<File>() {
      @Override public File run() {
        try {
          final InputStream is = getAssets().open("target/target.apk");

          File targetAPK = new File(AEasyDir.getDir(PITApp.this, "target"), "target.apk");
          EasyFile.createFile(targetAPK);

          IOUtils.transfer(is, new FileOutputStream(targetAPK), true);

          return targetAPK;
        } catch (Exception e) {
          PLLogger.w(TAG, e);
          return null;
        }
      }

      @Override public void onResult(File result) {
        if (result == null) {
          return;
        }

        Core.loadAPK(PITApp.this, result.getPath());
      }
    }).execute();
  }
}
