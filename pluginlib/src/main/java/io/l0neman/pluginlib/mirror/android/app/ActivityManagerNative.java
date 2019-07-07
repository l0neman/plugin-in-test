package io.l0neman.pluginlib.mirror.android.app;

import io.l0neman.pluginlib.mirror.android.util.Singleton;
import io.l0neman.pluginlib.util.reflect.mirror.MirrorClass;
import io.l0neman.pluginlib.util.reflect.mirror.annoation.MirrorClassName;

/**
 * Created by l0neman on 2019/07/07.
 */
@MirrorClassName("android.app.ActivityManagerNative")
public class ActivityManagerNative extends MirrorClass {
  public static Singleton gDefault;
}
