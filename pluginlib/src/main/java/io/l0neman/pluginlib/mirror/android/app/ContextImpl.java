package io.l0neman.pluginlib.mirror.android.app;

import android.content.res.Resources;

import io.l0neman.pluginlib.util.reflect.mirror.MirrorClass;
import io.l0neman.pluginlib.util.reflect.mirror.MirrorField;
import io.l0neman.pluginlib.util.reflect.mirror.annoation.TargetMirrorClassName;

/**
 * Created by l0neman on 2019/07/22.
 */
@TargetMirrorClassName("android.app.ContextImpl")
public class ContextImpl extends MirrorClass<Object> {
  public static final ContextImpl REUSE = ContextImpl.mapQuiet(ContextImpl.class);

  public MirrorField<Resources> mResources;

  public LoadedApk mPackageInfo;
}
