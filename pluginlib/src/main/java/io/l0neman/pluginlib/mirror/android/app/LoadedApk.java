package io.l0neman.pluginlib.mirror.android.app;

import android.content.res.Resources;

import io.l0neman.pluginlib.util.reflect.mirror.MirrorClass;
import io.l0neman.pluginlib.util.reflect.mirror.MirrorField;
import io.l0neman.pluginlib.util.reflect.mirror.annoation.TargetMirrorClassName;

/**
 * Created by l0neman on 2019/07/21.
 */
@TargetMirrorClassName("android.app.LoadedApk")
public class LoadedApk extends MirrorClass<Object> {
  public static final LoadedApk REUSE = LoadedApk.mapQuiet(LoadedApk.class);

  public MirrorField<Resources> mResources;
}
