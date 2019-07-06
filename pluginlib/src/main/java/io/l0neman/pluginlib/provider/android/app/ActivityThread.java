package io.l0neman.pluginlib.provider.android.app;


import android.os.Handler;

import io.l0neman.pluginlib.util.reflect.ReflectClass;
import io.l0neman.pluginlib.util.reflect.ReflectField;
import io.l0neman.pluginlib.util.reflect.annoation.MirrorClassName;

@MirrorClassName("android.app.ActivityThread")
public class ActivityThread extends ReflectClass {

  public static ReflectField sCurrentActivityThread;
  public ReflectField<Handler> mH;

  public static class H {
    public static final int LAUNCH_ACTIVITY = 100;
  }
}
