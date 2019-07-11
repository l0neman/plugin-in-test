package io.l0neman.pluginintest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.MotionEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import io.l0neman.pluginintest.activity.ATest;
import io.l0neman.pluginlib.Core;
import io.l0neman.pluginlib.support.PLLogger;
import io.l0neman.pluginlib.support.server.ServiceManager;
import io.l0neman.pluginlib.util.concurrent.EasyAsync;
import io.l0neman.pluginlib.util.file.AEasyDir;
import io.l0neman.pluginlib.util.file.EasyFile;
import io.l0neman.pluginlib.util.io.IOUtils;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "PluginIn";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

//    Core.initEnv(this);

    try {
      ServiceManager.getInstance().addService(this, "newService",
          new Binder() {
            @Override
            protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
              PLLogger.d(TAG, "service onTransact: code: " + code + " data: " +
                  data);
              return super.onTransact(code, data, reply, flags);
            }
          });
    } catch (RemoteException e) {
      PLLogger.w(TAG, "e");
    }
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    if (event.getActionMasked() == MotionEvent.ACTION_UP) {
//      ATest.jump(this);
//      releaseTargetAPK();
      try {
        IBinder newService = ServiceManager.getInstance().getService(this, "newService");
        Parcel data = Parcel.obtain();
        data.writeDouble(12.8D);
        newService.transact(89, data, Parcel.obtain(), IBinder.FLAG_ONEWAY);
      } catch (RemoteException e) {
        e.printStackTrace();
      }
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
