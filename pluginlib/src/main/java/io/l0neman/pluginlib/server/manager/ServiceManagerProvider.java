package io.l0neman.pluginlib.server.manager;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;

import androidx.core.app.BundleCompat;

import io.l0neman.pluginlib.content.VContext;
import io.l0neman.pluginlib.server.placeholder.PlaceholderManagerService;
import io.l0neman.pluginlib.support.PLLogger;

@SuppressWarnings("ALL")
public final class ServiceManagerProvider extends ContentProvider {

  private static final String TAG = ServiceManagerProvider.class.getSimpleName();

  public static final String AUTHORITIES = "content://io.l0neman.pluginlib.smprovider";
  public static final String KEY_BINDER = "binder";

  @Override public boolean onCreate() {

    try {
      ServiceManager.getInstance().addService(getContext(), VContext.PLACEHOLDER, new PlaceholderManagerService());
    } catch (RemoteException e) {
      PLLogger.e(TAG, "add service", e);
    }

    return true;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                      String sortOrder) { return null; }

  @Override public String getType(Uri uri) { return null; }

  @Override public Uri insert(Uri uri, ContentValues values) { return null; }

  @Override public int delete(Uri uri, String selection, String[] selectionArgs) { return 0; }

  @Override
  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) { return 0; }

  private ServiceManagerImpl mServiceManager = new ServiceManagerImpl();

  @Override public Bundle call(String method, String arg, Bundle extras) {
    Bundle service = new Bundle();
    BundleCompat.putBinder(service, KEY_BINDER, mServiceManager);
    PLLogger.d("SMProvider", "provider: put service manager impl.");
    return service;
  }
}
