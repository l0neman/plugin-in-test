package io.l0neman.pluginlib.util.reflect.mirror;

import androidx.collection.ArrayMap;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Map;

import io.l0neman.pluginlib.util.Reflect;
import io.l0neman.pluginlib.util.reflect.mirror.throwable.MirrorException;
import io.l0neman.pluginlib.util.reflect.mirror.util.MethodInfo;

/**
 * Created by l0neman on 2019/07/18.
 */
public class MirrorConstructor<T> {
  private Constructor<T> mConstructor;
  private Map<String, Constructor<T>> mOverloadConstructorMap;

  public MirrorConstructor(Constructor<T>[] overloadConstructor) {
    if (overloadConstructor.length == 1) {
      this.mConstructor = overloadConstructor[0];
      return;
    }

    mOverloadConstructorMap = new ArrayMap<>();

    for (Constructor<T> constructor : overloadConstructor) {
      mOverloadConstructorMap.put(
          MethodInfo.getSignature("", constructor.getParameterTypes()),
          constructor);
    }
  }

  public boolean isOverload() {
    return mOverloadConstructorMap != null;
  }

  public T newInstance() throws MirrorException {
    try {
      return Reflect.with(mConstructor).create();
    } catch (Reflect.ReflectException e) {
      throw new MirrorException("newInstance", e);
    }
  }

  public T newInstance(Object... args) throws MirrorException {
    try {
      return Reflect.with(mConstructor).create();
    } catch (Reflect.ReflectException e) {
      throw new MirrorException("newInstance", e);
    }
  }

  public T newInstance(Class<?>[] parameterTypes, Object... args) throws MirrorException {
    Constructor<T> constructor = mOverloadConstructorMap.get(MethodInfo.getSignature("", parameterTypes));
    if (constructor == null) {
      throw new MirrorException("not found method: " + Arrays.toString(parameterTypes));
    }

    try {
      return Reflect.with(constructor).create(args);
    } catch (Exception e) {
      throw new MirrorException(e);
    }
  }
}
