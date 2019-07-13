package io.l0neman.pluginintest;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import io.l0neman.pluginlib.Core;
import io.l0neman.pluginlib.support.PLLogger;
import io.l0neman.pluginlib.util.concurrent.EasyAsync;
import io.l0neman.pluginlib.util.file.AEasyDir;
import io.l0neman.pluginlib.util.file.EasyFile;
import io.l0neman.pluginlib.util.io.IOUtils;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "PluginIn";

  @Override protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(newBase);
    Core.getInstance().initEnv(newBase);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    prepareAPK();
  }

  private void prepareAPK() {
    EasyAsync.get(new EasyAsync.Executor<File>() {
      @Override public File run() {
        try {
          final InputStream is = getAssets().open("target/target.apk");

          File targetAPK = new File(AEasyDir.getDir(MainActivity.this, "target"), "target.apk");

          if (targetAPK.exists()) {
            return targetAPK;
          }

          EasyFile.createFile(targetAPK);

          IOUtils.transfer(is, new FileOutputStream(targetAPK), true);

          return targetAPK;
        } catch (Exception e) {
          PLLogger.w("", e);
          return null;
        }
      }

      @Override public void onResult(File result) {
        if (result == null) {
          return;
        }

        Core.getInstance().preloadAPK(MainActivity.this, result.getPath());
      }
    }).execute();
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    if (event.getActionMasked() == MotionEvent.ACTION_UP) {
//      ATest.jump(this);
      Core.getInstance().launchAPK(this);
    }

    return super.onTouchEvent(event);
  }
}
