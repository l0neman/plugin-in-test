package io.l0neman.pluginlib.mirror.android.content.pm;

import java.io.File;

import io.l0neman.pluginlib.util.reflect.mirror.MirrorClass;
import io.l0neman.pluginlib.util.reflect.mirror.MirrorMethod;
import io.l0neman.pluginlib.util.reflect.mirror.annoation.TargetMirrorClassName;
import io.l0neman.pluginlib.util.reflect.mirror.annoation.MethodParameterTypes;

/**
 * Created by l0neman on 2019/07/07.
 */
@TargetMirrorClassName(PackageParser.MIRROR_CLASS)
public class PackageParser extends MirrorClass<Object> {

  public static final String MIRROR_CLASS = "android.content.pm.PackageParser";

  public PackageParser() {
    construct(PackageParser.class);
  }

  public Object parsePackage(File packageFile, int flags) {
    return invoke("parsePackage", $(File.class, int.class), packageFile, flags);
  }
}
