package io.l0neman.pluginlib.support.server;

import android.os.IBinder;
import android.os.RemoteException;

public class ServiceManagerImpl extends IServiceManager.Stub {
  private ServiceRegistry mServiceRegistry = new ServiceRegistry();

  @Override public void addService(String name, IBinder binder) throws RemoteException {
    mServiceRegistry.registerService(name, binder);
  }

  @Override public IBinder getService(String name) throws RemoteException {
    return mServiceRegistry.getService(name);
  }
}
