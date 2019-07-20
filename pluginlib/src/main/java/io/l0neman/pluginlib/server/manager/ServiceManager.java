package io.l0neman.pluginlib.server.manager;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.core.app.BundleCompat;

import io.l0neman.pluginlib.support.PLLogger;

public class ServiceManager {

  private static final String TAG = ServiceManager.class.getSimpleName();

  private static ServiceManager sInstance = new ServiceManager();

  private ServiceManager() {}

  public static ServiceManager getInstance() {
    return sInstance;
  }

  private IServiceManager mIServiceManager;

  private void ensureIServiceManager(final Context context) throws RemoteException {
    Bundle bundle = context.getContentResolver().call(
        Uri.parse(ServiceManagerProvider.AUTHORITIES), "$", null, new Bundle());

    if (bundle == null) {
      throw new RuntimeException("getSignature service manager is null. [Bundle]");
    }

    IBinder serviceManager = BundleCompat.getBinder(bundle, ServiceManagerProvider.KEY_BINDER);

    if (serviceManager == null) {
      throw new RuntimeException("getSignature service manager is null [getBinder].");
    }

    serviceManager.linkToDeath(new IBinder.DeathRecipient() {
      @Override public void binderDied() {
        try {
          ensureIServiceManager(context);
        } catch (RemoteException e) {
          PLLogger.w(TAG, "");
        }
      }
    }, 0);

    mIServiceManager = IServiceManager.Stub.asInterface(serviceManager);
  }

  private IServiceManager getIServiceManager(Context context) throws RemoteException {
    ensureIServiceManager(context);
    return mIServiceManager;
  }

  public void addService(Context context, String name, IBinder binder) throws RemoteException {
    getIServiceManager(context).addService(name, binder);
  }

  public IBinder getService(Context context, String name) throws RemoteException {
    return getIServiceManager(context).getService(name);
  }
}
