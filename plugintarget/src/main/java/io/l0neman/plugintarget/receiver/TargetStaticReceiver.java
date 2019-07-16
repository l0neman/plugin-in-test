package io.l0neman.plugintarget.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import io.l0neman.plugintarget.util.TALogger;

public class TargetStaticReceiver extends BroadcastReceiver {
  private static final String TAG = TargetStaticReceiver.class.getSimpleName();

  @Override public void onReceive(Context context, Intent intent) {
    TALogger.d(TAG, "receive: " + intent);
  }
}
