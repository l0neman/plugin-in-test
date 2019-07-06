package io.l0neman.pluginlib.provider.android.util;

import io.l0neman.pluginlib.util.reflect.ReflectClass;
import io.l0neman.pluginlib.util.reflect.ReflectField;
import io.l0neman.pluginlib.util.reflect.annoation.MirrorClassName;

/**
 * Created by l0neman on 2019/07/07.
 */
@MirrorClassName("android.util.Singleton")
public class Singleton extends ReflectClass {
  public ReflectField mInstance;
}
