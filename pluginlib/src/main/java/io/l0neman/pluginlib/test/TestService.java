package io.l0neman.pluginlib.test;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import io.l0neman.pluginlib.support.PLLogger;

public class TestService extends Service {

  private static final String TAG = TestService.class.getSimpleName();

  public TestService() {
  }

  @Override public void onCreate() {
    super.onCreate();
    PLLogger.d(TAG, "onCreate");
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    PLLogger.d(TAG, "onStartCommand");
    return super.onStartCommand(intent, flags, startId);
  }

  @Override public void onDestroy() {
    super.onDestroy();
    PLLogger.d(TAG, "onDestroy");
  }

  @Override
  public IBinder onBind(Intent intent) {
    throw new UnsupportedOperationException("Not yet implemented");
  }
}
