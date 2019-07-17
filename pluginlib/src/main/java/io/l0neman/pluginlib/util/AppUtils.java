package io.l0neman.pluginlib.util;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;

import java.io.File;
import java.util.List;

import io.l0neman.pluginlib.mirror.android.content.pm.PackageParser;
import io.l0neman.pluginlib.support.PLLogger;

/**
 * Created by l0neman on 2019/07/15.
 */
public class AppUtils {

  private static final String TAG = AppUtils.class.getSimpleName();

  public static String findMainActivity(String apkPath) throws Exception {
    try {
      Object packageParserObject = Reflect.with(PackageParser.MIRROR_CLASS).creator().create();
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

}
