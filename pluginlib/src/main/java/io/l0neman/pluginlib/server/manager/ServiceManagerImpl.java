package io.l0neman.pluginlib.server.manager;

import android.os.IBinder;

import androidx.collection.ArrayMap;

import java.util.Map;


public class ServiceManagerImpl extends IServiceManager.Stub {
  private Map<String, IBinder> mServiceMap = new ArrayMap<>();

  @Override public void addService(String name, IBinder binder) {
    mServiceMap.put(name, binder);
  }

  @Override public IBinder getService(String name) {
    return mServiceMap.get(name);
  }
}
