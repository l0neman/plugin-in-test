package io.l0neman.pluginlib.provider.android.app;

import io.l0neman.pluginlib.provider.android.util.Singleton;
import io.l0neman.pluginlib.util.reflect.ReflectClass;
import io.l0neman.pluginlib.util.reflect.annoation.MirrorClassName;

/**
 * Created by l0neman on 2019/07/07.
 */
@MirrorClassName("android.app.ActivityManagerNative")
public class ActivityManagerNative extends ReflectClass {
  public static Singleton gDefault;
}
