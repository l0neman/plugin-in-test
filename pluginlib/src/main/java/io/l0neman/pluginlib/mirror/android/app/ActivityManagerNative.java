package io.l0neman.pluginlib.mirror.android.app;

import io.l0neman.pluginlib.mirror.android.util.Singleton;
import io.l0neman.pluginlib.util.reflect.mirror.MirrorClass;
import io.l0neman.pluginlib.util.reflect.mirror.annoation.TargetMirrorClassName;

/**
 * Created by l0neman on 2019/07/07.
 */
@TargetMirrorClassName("android.app.ActivityManagerNative")
public class ActivityManagerNative extends MirrorClass<Object> {
  public static final ActivityManagerNative REUSE = ActivityManagerNative.mapQuiet(ActivityManagerNative.class);

  public static Singleton gDefault;
}
