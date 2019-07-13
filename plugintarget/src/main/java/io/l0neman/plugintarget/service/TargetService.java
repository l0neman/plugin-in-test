package io.l0neman.plugintarget.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import io.l0neman.plugintarget.util.TALogger;

public class TargetService extends Service {
  private static final String TAG = TargetService.class.getSimpleName();

  public TargetService() {
    TALogger.d(TAG, "TargetService <init>");
  }

  @Override public void onCreate() {
    super.onCreate();
    TALogger.d(TAG, "onCreate");
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    TALogger.d(TAG, "onStartCommand");
    return super.onStartCommand(intent, flags, startId);
  }

  @Override public void onDestroy() {
    super.onDestroy();
    TALogger.d(TAG, "onDestroy");
  }

  @Override
  public IBinder onBind(Intent intent) {
    return new Binder() ;
  }
}
