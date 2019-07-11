package io.l0neman.pluginlib.support.server;

import android.os.IBinder;

import androidx.collection.ArrayMap;

import java.util.Map;

public class ServiceRegistry {

  private Map<String, IBinder> mServiceMap = new ArrayMap<>();

  public void registerService(String name, IBinder binder) {
    mServiceMap.put(name, binder);
  }

  public IBinder getService(String name) {
    return mServiceMap.get(name);
  }
}
