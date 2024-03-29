package io.l0neman.pluginlib;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Resources;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Map;

import io.l0neman.pluginlib.content.VContext;
import io.l0neman.pluginlib.hook.android.app.ActivityManagerNativeHook;
import io.l0neman.pluginlib.hook.android.app.ActivityThreadHook;
import io.l0neman.pluginlib.mirror.android.app.ActivityThread;
import io.l0neman.pluginlib.mirror.android.app.ContextImpl;
import io.l0neman.pluginlib.mirror.android.app.LoadedApk;
import io.l0neman.pluginlib.mirror.android.content.res.AssetManager;
import io.l0neman.pluginlib.support.PLLogger;
import io.l0neman.pluginlib.support.Process;
import io.l0neman.pluginlib.util.AppUtils;
import io.l0neman.pluginlib.util.ClassLoaderUtils;
import io.l0neman.pluginlib.util.reflect.Reflect;
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

  public ActivityThread currentActivityThread() {
    return mMainThread;
  }

  public Context getHostContext() {
    return mHostContext;
  }

  public void initEnv(Application context) {
    mHostContext = context;
    VContext.getInstance().init(context);

    try {
      mMainThread = MirrorClass.map(ActivityThread.sCurrentActivityThread.get(), ActivityThread.class);
    } catch (MirrorException e) {
      throw new RuntimeException(e);
    }
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

    try {
      final String application = AppUtils.getInstance().findApplication(mApkPath);
      PLLogger.d(TAG, "find application: " + application);
      Application app = Reflect.with(application).creator().create();
      try {
        Reflect.with(app).invoker().method("attachBaseContext")
            .paramsType(Context.class)
            .invoke(getHostContext());
      } catch (Exception e) {
        PLLogger.w(TAG, e);
      }

      app.onCreate();

    } catch (Exception e) {
      PLLogger.w(TAG, "call Application", e);
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

  public void preload() {
    if (!Process.isServerProcess()) {
      ActivityManagerNativeHook.hook();
      ActivityThreadHook.H.hook();
    }

    detectProcess();
  }

  private void detectProcess() {
    if (Process.isMainProcess()) {
      PLLogger.i(TAG, "in main process.");
      return;
    }

    final String tApkPath = SPUtils.get(getHostContext(), "apkInfo")
        .getString("tApkPath", null);

    if (Process.isAppProcess()) {
      PLLogger.i(TAG, "in app process.");
    }

    prepareResources(tApkPath);
    preloadAPK(getHostContext(), tApkPath);
  }

  private void prepareResources(String apkPath) {
    try {
      final Map<String, WeakReference> mPackages = mMainThread.mPackages.get();
      final WeakReference loadedApkRef = mPackages.get(getHostContext().getPackageName());

      PLLogger.d(TAG, "loaded apk: " + Arrays.toString(mPackages.keySet().toArray()));

      final Object loadedApk = loadedApkRef.get();
      LoadedApk mirrorLoadedApk = LoadedApk.REUSE.attach(loadedApk);

      AssetManager assetManager = new AssetManager();
      assetManager.addAssetPath(apkPath);

//      try {
//        final InputStream open = assetManager.open("xiconhelper.apk");
//        PLLogger.d(TAG, "open xiconhelper.apk ok.");
//        open.close();
//      }catch (Exception e){
//        PLLogger.w(TAG, "open xiconhelper.apk", e);
//      }

      final Resources oldResources = getHostContext().getResources();
      Resources resources = new Resources(assetManager.getTargetMirrorObject(), oldResources.getDisplayMetrics(),
          oldResources.getConfiguration());

      final Object mTBase = Reflect.with(getHostContext()).injector()
          .field("mBase")
          .get();

      final ContextImpl mBase = ContextImpl.REUSE.attach(mTBase);

      mBase.mPackageInfo.mResources.set(resources);
      mirrorLoadedApk.mResources.set(resources);

      PLLogger.d("res1", "" + mBase.mPackageInfo.mResources.get());
      PLLogger.d("res2", "" + mirrorLoadedApk.mResources.get());

      PLLogger.d(TAG, "add asset path: " + apkPath);
    } catch (Exception e) {
      PLLogger.w(TAG, "prepareResources", e);
    }
  }
}
