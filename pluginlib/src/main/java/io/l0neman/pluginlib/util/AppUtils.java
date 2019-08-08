package io.l0neman.pluginlib.util;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;

import java.io.File;
import java.util.List;

import io.l0neman.pluginlib.mirror.android.content.pm.PackageParser;
import io.l0neman.pluginlib.support.PLLogger;
import io.l0neman.pluginlib.util.reflect.Reflect;

/**
 * Created by l0neman on 2019/07/15.
 */
public class AppUtils {

  private static final String TAG = AppUtils.class.getSimpleName();

  private static Singleton<AppUtils> sSingleton = new Singleton<AppUtils>() {
    @Override protected AppUtils create() {
      return new AppUtils();
    }
  };

  private AppUtils() {}

  public static AppUtils getInstance() {
    return sSingleton.get();
  }

  private PackageParser mPackageParser = new PackageParser();

  public String findMainActivity(String apkPath) throws Exception {
    try {
      // find main activity class.
      final Object packageObject = mPackageParser.parsePackage(new File(apkPath), 0);

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
      PLLogger.w(TAG, "find main activity", e);
      throw new Exception(e);
    }
  }

  public String findApplication(String apkPath) throws Exception {
    try {
      final Object parsePackage = mPackageParser.parsePackage(new File(apkPath), 0);
      ApplicationInfo appInfo = Reflect.with(parsePackage).injector().field("applicationInfo").get();
      if (appInfo == null) {
        return null;
      }

      return appInfo.name;
    } catch (Exception e) {
      PLLogger.w(TAG, "find application", e);
      return null;
    }
  }
}
