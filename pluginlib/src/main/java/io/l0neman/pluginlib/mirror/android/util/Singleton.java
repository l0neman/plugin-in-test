package io.l0neman.pluginlib.mirror.android.util;

import io.l0neman.pluginlib.util.reflect.mirror.MirrorClass;
import io.l0neman.pluginlib.util.reflect.mirror.MirrorField;
import io.l0neman.pluginlib.util.reflect.mirror.annoation.TargetMirrorClassName;

/**
 * Created by l0neman on 2019/07/07.
 */
@TargetMirrorClassName("android.util.Singleton")
public class Singleton extends MirrorClass {
  public MirrorField mInstance;
}
