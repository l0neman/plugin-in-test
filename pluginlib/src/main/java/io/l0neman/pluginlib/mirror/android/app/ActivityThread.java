package io.l0neman.pluginlib.mirror.android.app;


import android.os.Handler;

import io.l0neman.pluginlib.util.reflect.mirror.MirrorClass;
import io.l0neman.pluginlib.util.reflect.mirror.MirrorField;
import io.l0neman.pluginlib.util.reflect.mirror.annoation.TargetMirrorClassName;

@TargetMirrorClassName("android.app.ActivityThread")
public class ActivityThread extends MirrorClass {

  static {
    mapQuiet(ActivityThread.class);
  }

  public static MirrorField sCurrentActivityThread;
  public MirrorField<Handler> mH;

  public String getProcessName() {
    return invoke("getProcessName");
  }

  public static class H {
    public static final int LAUNCH_ACTIVITY = 100;
    public static final int CREATE_SERVICE = 114;
  }
}
