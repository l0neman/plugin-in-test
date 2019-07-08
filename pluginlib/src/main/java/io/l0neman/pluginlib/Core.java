package io.l0neman.pluginlib;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;

import java.io.File;
import java.util.List;

import io.l0neman.pluginlib.mirror.android.content.pm.PackageParser;
import io.l0neman.pluginlib.support.PLLogger;
import io.l0neman.pluginlib.util.ClassLoaderUtils;
import io.l0neman.pluginlib.util.Reflect;

public final class Core {

  public static final String TAG = Core.class.getSimpleName();

  public static void initEnv(Context context) {
    TestHooker.hook();
  }

  public static void loadAPK(Context context, String apkPath) {
    PLLogger.d(TAG, "load: " + apkPath);

    try {
      ClassLoaderUtils.insertCode(context, context.getClassLoader(), apkPath);
      PLLogger.d(TAG, "insert code.");
      launchTargetActivity(context, apkPath);
    } catch (Exception e) {
      PLLogger.w(TAG, "insert code: " + e);
    }
  }

  private static String findMainActivity(String apkPath) throws Exception {
    try {
      Object packageParserObject = Reflect.with("android.content.pm.PackageParser").builder().build();
      final PackageParser packageParser = PackageParser.mirror(packageParserObject, PackageParser.class);

      // find main activity class.
      final Object packageObject = packageParser.parsePackage(new File(apkPath), 0);

      List activities = Reflect.with(packageObject).injector().field("activities").get();

      for (Object activity : activities) {
        List<IntentFilter> intentFilters = Reflect.with(activity).injector().field("intents").get();

        for (IntentFilter ii : intentFilters) {
          if (ii.hasAction(Intent.ACTION_MAIN) && ii.hasCategory(Intent.CATEGORY_LAUNCHER)) {
            ActivityInfo info = Reflect.with(activity).injector().field("info").get();
            final String className = info.name;
            PLLogger.d(TAG, "find main activity class: " + className);
            return className;
          }
        }
      }
      throw new Exception("not found main activity.");
    } catch (Exception e) {
      PLLogger.w(TAG, "findMainActivity: ", e);
      throw new Exception(e);
    }
  }

  private static void launchTargetActivity(Context context, String apkPath) {
    final String mainActivityName;
    try {
      mainActivityName = findMainActivity(apkPath);
      Class<?> clazz = Reflect.with(mainActivityName).getClazz();
      System.out.println(clazz);

      try {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(
            context.getPackageManager()
                .getPackageArchiveInfo(apkPath, 0).packageName,
            mainActivityName
        ));
        context.startActivity(intent);
      } catch (Throwable e) {
        PLLogger.e(TAG, "start activity", e);
      }
    } catch (Exception e) {
      PLLogger.w(TAG, "launchTargetActivity", e);
    }
  }
}
