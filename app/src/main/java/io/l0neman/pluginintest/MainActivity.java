package io.l0neman.pluginintest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import io.l0neman.pluginintest.activity.ATest;
import io.l0neman.pluginlib.Core;
import io.l0neman.pluginlib.support.PLLogger;
import io.l0neman.pluginlib.util.concurrent.EasyAsync;
import io.l0neman.pluginlib.util.file.AEasyDir;
import io.l0neman.pluginlib.util.file.EasyFile;
import io.l0neman.pluginlib.util.io.IOUtils;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);


    Core.initEnv(this);
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    if(event.getActionMasked() == MotionEvent.ACTION_UP) {
//      ATest.jump(this);
      releaseTargetAPK();
    }

    return super.onTouchEvent(event);
  }

  private void releaseTargetAPK() {
    EasyAsync.get(new EasyAsync.Executor<File>() {
      @Override public File run() {
        try {
          final InputStream is = getAssets().open("target/target.apk");

          File targetAPK = new File(AEasyDir.getDir(MainActivity.this, "target"), "target.apk");
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

        Core.loadAPK(MainActivity.this, result.getPath());
      }
    }).execute();
  }
}
