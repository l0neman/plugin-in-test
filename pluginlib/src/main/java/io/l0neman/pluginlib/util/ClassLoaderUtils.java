package io.l0neman.pluginlib.util;

import android.content.Context;

import java.io.File;
import java.lang.reflect.Array;

import dalvik.system.DexClassLoader;
import io.l0neman.pluginlib.support.PLLogger;
import io.l0neman.pluginlib.util.file.AEasyDir;
import io.l0neman.pluginlib.util.file.EasyFile;
import io.l0neman.pluginlib.util.reflect.Reflect;

/**
 * Created by l0neman on 2019/07/07.
 */
public class ClassLoaderUtils {

  private static final String TAG = ClassLoaderUtils.class.getSimpleName();

  private static final String OPT_DIR_NAME = "targetOpt";

  private static Object[] getDexElements(ClassLoader classLoader) throws Exception {

    Object pathList = Reflect.with(classLoader).injector().field("pathList").get();

    return (Object[]) Reflect.with(pathList).injector().field("dexElements").get();
  }

  private static Object[] insert(Object[] insert, Object[] target) {
    int iL = insert.length;
    int tL = target.length;

    Object[] newDexElements = (Object[])
        Array.newInstance(insert.getClass().getComponentType(), iL + tL);

    System.arraycopy(insert, 0, newDexElements, 0, insert.length);
    System.arraycopy(target, 0, newDexElements, insert.length, target.length);

    return newDexElements;
  }

  public static void insertCode(Context context, ClassLoader classLoader, String codePackPath)
      throws Exception {
    Object[] srcDexElements = getDexElements(classLoader);

    File optDir = new File(AEasyDir.getDir(context, "opt"), OPT_DIR_NAME);

    EasyFile.deleteFile(optDir);
    EasyFile.createDir(optDir);

    String optimizedDirectory = optDir.getPath();

    PLLogger.d(TAG, "optDirectory: " + optimizedDirectory);

    DexClassLoader sdkClassLoader = new DexClassLoader(
        codePackPath, optimizedDirectory, null, classLoader);

    Object[] sdkDexElements = getDexElements(sdkClassLoader);
    Object newDexElements = insert(sdkDexElements, srcDexElements);

    set(classLoader, newDexElements);
  }

  private static void set(ClassLoader classLoader, Object dexElements) throws Exception {
    Object pathList = Reflect.with(classLoader).injector().field("pathList").get();

    Reflect.with(pathList).injector().field("dexElements").set(dexElements);
  }
}
