package io.l0neman.pluginlib.mirror.android.content.res;

import io.l0neman.pluginlib.util.reflect.mirror.MirrorClass;
import io.l0neman.pluginlib.util.reflect.mirror.annoation.TargetMirrorClass;

/**
 * Created by l0neman on 2019/07/22.
 */
@TargetMirrorClass(android.content.res.AssetManager.class)
public class AssetManager extends MirrorClass<android.content.res.AssetManager> {

  public AssetManager() {
    construct(AssetManager.class);
  }

  public void addAssetPath(String path) {
    invoke("addAssetPath", $(String.class), path);
  }
}