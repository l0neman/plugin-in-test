package io.l0neman.pluginlib.server.manager;

import android.os.IBinder;
import android.os.RemoteException;

import androidx.collection.ArrayMap;

import java.util.Map;


public class ServiceManagerImpl extends IServiceManager.Stub {
  private Map<String, IBinder> mServiceMap = new ArrayMap<>();

  @Override public void addService(String name, IBinder binder) throws RemoteException {
    mServiceMap.put(name, binder);
  }

  @Override public IBinder getService(String name) throws RemoteException {
    return mServiceMap.get(name);
  }
}
