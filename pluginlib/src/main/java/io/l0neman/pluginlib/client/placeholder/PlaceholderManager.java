package io.l0neman.pluginlib.client.placeholder;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;

import io.l0neman.pluginlib.content.VContext;
import io.l0neman.pluginlib.server.manager.ServiceManager;
import io.l0neman.pluginlib.server.placeholder.IPlaceholderManager;
import io.l0neman.pluginlib.support.PLLogger;
import io.l0neman.pluginlib.util.Singleton;

/**
 * Created by l0neman on 2019/07/20.
 */
public class PlaceholderManager {

  private static final String TAG = PlaceholderManager.class.getSimpleName();

  private final Context mContext;

  public PlaceholderManager(Context mContext) {
    this.mContext = mContext;
  }

  private Singleton<IPlaceholderManager> mDefault = new Singleton<IPlaceholderManager>() {
    @Override protected IPlaceholderManager create() {
      final IBinder service;

      try {
        service = ServiceManager.getInstance().getService(mContext, VContext.PLACEHOLDER);
      } catch (RemoteException e) {
        PLLogger.e(TAG, "createService", e);
        return null;
      }

      return IPlaceholderManager.Stub.asInterface(service);
    }
  };

  private IPlaceholderManager getIPlaceholderManager() {
    return mDefault.get();
  }

  public String queryKeyActivity(String appliedActivityName) throws RemoteException {
    return getIPlaceholderManager().queryKeyActivity(appliedActivityName);
  }

  public String queryKeyService(String appliedServiceName) throws RemoteException {
    return getIPlaceholderManager().queryKeyService(appliedServiceName);
  }

  public String applyActivity(String keyActivityName) throws RemoteException {
    return getIPlaceholderManager().applyActivity(keyActivityName);
  }

  public String applyService(String keyServiceName) throws RemoteException {
    return getIPlaceholderManager().applyService(keyServiceName);
  }
}
