package io.l0neman.pluginlib;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;

import io.l0neman.pluginlib.hook.android.app.ActivityManagerNativeHook;
import io.l0neman.pluginlib.hook.android.app.ActivityThreadHook;
import io.l0neman.pluginlib.support.PLLogger;
import io.l0neman.pluginlib.util.AppUtils;
import io.l0neman.pluginlib.util.ClassLoaderUtils;
import io.l0neman.pluginlib.util.Reflect;

public final class Core {

  public static final String TAG = Core.class.getSimpleName();

  private static Core sCore = new Core();

  public static Core getInstance() {
    return sCore;
  }

  private String mApkPath;
  private Context mHostContext;

  public void initEnv(Context context) {
    mHostContext = context;
    ActivityManagerNativeHook.hook();
    ActivityThreadHook.H.hook();
  }

  public Context getHostContext() {
    return mHostContext;
  }

  public void preloadAPK(Context context, String apkPath) {
    PLLogger.d(TAG, "load: " + apkPath);

    try {
      ClassLoaderUtils.insertCode(context, context.getClassLoader(), apkPath);
      PLLogger.d(TAG, "insert code.");

      this.mApkPath = apkPath;

    } catch (Exception e) {
      PLLogger.w(TAG, "insert code: " + e);
    }
  }

  public void launchAPK(Context context) {
    final String mainActivityName;
    try {
      mainActivityName = AppUtils.findMainActivity(mApkPath);

      Class<?> clazz = Reflect.with(mainActivityName).getClazz();
      PLLogger.d(TAG, "find main activity" + clazz.getName());

      try {
        Intent intent = new Intent();
        final PackageInfo pi = context.getPackageManager().getPackageArchiveInfo(mApkPath, 0);

        if (pi == null) {
          return;
        }

        intent.setComponent(new ComponentName(pi.packageName, mainActivityName));
        context.startActivity(intent);
      } catch (Throwable e) {
        PLLogger.e(TAG, "start activity", e);
      }
    } catch (Exception e) {
      PLLogger.w(TAG, "launchTargetActivity", e);
    }
  }
}
