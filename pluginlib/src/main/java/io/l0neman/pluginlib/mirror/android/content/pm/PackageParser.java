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
public class PackageParser extends MirrorClass {

  public static final String MIRROR_CLASS = "android.content.pm.PackageParser";

  @MethodParameterTypes({File.class, int.class})
  public MirrorMethod parsePackage;

  public PackageParser() {
    construct(null);
  }

  public Object parsePackage(File packageFile, int flags) {
    return invoke("parsePackage", new Class[]{File.class, int.class}, packageFile, flags);
  }
}
