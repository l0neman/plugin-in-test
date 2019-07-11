package io.l0neman.pluginlib;

import io.l0neman.pluginlib.hook.android.app.ActivityManager;
import io.l0neman.pluginlib.hook.android.app.ActivityThread;

/**
 * Created by l0neman on 2019/07/07.
 */
public class TestHooker {

  public static void hook() {
    ActivityManager.hook();
    ActivityThread.H.hook();
  }
}
