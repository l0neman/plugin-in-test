package io.l0neman.pluginlib.support;

import android.content.Context;

import io.l0neman.pluginlib.Core;

/**
 * Created by l0neman on 2019/07/20.
 */
public class Process {

  private static final String TAG = Process.class.getSimpleName();

  private static final String APP_PROCESS_SUFFIX = ":c";
  private static final String SERVER_PROCESS_SUFFIX = ":s";

  public static boolean isMainProcess() {
    final String mainProcessName = Core.getInstance().getHostContext().getApplicationInfo().processName;
    final String processName = Core.getInstance().currentActivityThread().getProcessName();

    PLLogger.d(TAG, "current process: " + processName);

    return processName.equals(mainProcessName);
  }

  public static boolean isAppProcess() {
    final String processName = Core.getInstance().currentActivityThread().getProcessName();

    return processName.startsWith(Core.getInstance().getHostContext().getPackageName() + APP_PROCESS_SUFFIX);
  }

  public static boolean isServerProcess() {
    final String processName = Core.getInstance().currentActivityThread().getProcessName();

    return processName.endsWith(SERVER_PROCESS_SUFFIX);
  }
}
