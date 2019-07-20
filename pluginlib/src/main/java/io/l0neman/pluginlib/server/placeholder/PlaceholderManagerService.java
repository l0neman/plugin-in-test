package io.l0neman.pluginlib.server.placeholder;

import android.os.RemoteException;

import io.l0neman.pluginlib.support.placeholder.PlaceholderHelper;

/**
 * Created by l0neman on 2019/07/20.
 */
public class PlaceholderManagerService extends IPlaceholderManager.Stub {

  @Override public String queryKeyActivity(String appliedActivityName) throws RemoteException {
    return PlaceholderHelper.getInstance().queryKeyActivity(appliedActivityName);
  }

  @Override public String queryKeyService(String appliedServiceName) throws RemoteException {
    return PlaceholderHelper.getInstance().queryKeyService(appliedServiceName);
  }

  @Override public String applyActivity(String keyActivityName) throws RemoteException {
    return PlaceholderHelper.getInstance().applyActivity(keyActivityName);
  }

  @Override public String applyService(String keyServiceName) throws RemoteException {
    return PlaceholderHelper.getInstance().applyService(keyServiceName);
  }
}
