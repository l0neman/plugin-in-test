package io.l0neman.pluginlib.mirror.android.app;


import android.os.Handler;

import io.l0neman.pluginlib.util.reflect.mirror.MirrorClass;
import io.l0neman.pluginlib.util.reflect.mirror.MirrorField;
import io.l0neman.pluginlib.util.reflect.mirror.annoation.MirrorClassName;

@MirrorClassName("android.app.ActivityThread")
public class ActivityThread extends MirrorClass {

  public static MirrorField sCurrentActivityThread;
  public MirrorField<Handler> mH;

  public static class H {
    public static final int LAUNCH_ACTIVITY = 100;
  }
}
