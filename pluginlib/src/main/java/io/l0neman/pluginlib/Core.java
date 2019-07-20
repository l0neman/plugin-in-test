package io.l0neman.pluginlib;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;

import io.l0neman.pluginlib.content.VContext;
import io.l0neman.pluginlib.hook.android.app.ActivityManagerNativeHook;
import io.l0neman.pluginlib.hook.android.app.ActivityThreadHook;
import io.l0neman.pluginlib.mirror.android.app.ActivityThread;
import io.l0neman.pluginlib.support.PLLogger;
import io.l0neman.pluginlib.support.Process;
import io.l0neman.pluginlib.util.AppUtils;
import io.l0neman.pluginlib.util.ClassLoaderUtils;
import io.l0neman.pluginlib.util.Reflect;
import io.l0neman.pluginlib.util.SPUtils;
import io.l0neman.pluginlib.util.reflect.mirror.MirrorClass;
import io.l0neman.pluginlib.util.reflect.mirror.throwable.MirrorException;

public final class Core {

  public static final String TAG = Core.class.getSimpleName();

  private static Core sCore = new Core();

  public static Core getInstance() {
    return sCore;
  }

  private String mApkPath;
  private Application mHostContext;
  private ActivityThread mMainThread;

  public static volatile boolean sHook = true;

  public void initEnv(Application context) {
    mHostContext = context;
    VContext.getInstance().init(context);

    try {
      mMainThread = MirrorClass.map(ActivityThread.sCurrentActivityThread.get(), ActivityThread.class);
    } catch (MirrorException e) {
      throw new RuntimeException(e);
    }

    if (Process.isMainProcess()) {
      PLLogger.d(TAG, "hook kkkkkkkkkk");

      ActivityManagerNativeHook.hook();
      ActivityThreadHook.H.hook();
    }

    detectProcess();
  }

  public ActivityThread currentActivityThread() {
    return mMainThread;
  }

  private void detectProcess() {
    if (Process.isMainProcess()) {
      PLLogger.i(TAG, "in main process.");
    } else {

      preloadAPK(getHostContext(), SPUtils.get(getHostContext(), "apkInfo")
          .getString("tApkPath", null));

      if (Process.isAppProcess()) {
        PLLogger.i(TAG, "in app process.");
      }
    }
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
      SPUtils.apply(getHostContext(), "apkInfo", "tApkPath", mApkPath);

    } catch (Exception e) {
      PLLogger.w(TAG, "insert code: " + e);
    }
  }

  public void launchAPK(Context context) {
    final String mainActivityName;
    try {
      mainActivityName = AppUtils.getInstance().findMainActivity(mApkPath);

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
