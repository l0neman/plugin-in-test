package io.l0neman.plugintarget.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.l0neman.plugintarget.util.TALogger;

public class TargetProvider extends ContentProvider {
  private static final String TAG = TargetProvider.class.getSimpleName();

  public TargetProvider() {
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    // Implement this to handle requests to delete one or more rows.
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public String getType(Uri uri) {
    // at the given URI.
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public Uri insert(Uri uri, ContentValues values) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public boolean onCreate() {
    TALogger.d(TAG, "onCreate");
    return true;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection,
                      String[] selectionArgs, String sortOrder) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection,
                    String[] selectionArgs) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Nullable @Override
  public Bundle call(@NonNull String method, @Nullable String arg, @Nullable Bundle extras) {
    TALogger.d(TAG, "call method: " + method + " arg: " + arg);
    return new Bundle();
  }
}
