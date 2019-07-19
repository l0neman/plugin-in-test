package io.l0neman.pluginlib.util.reflect.mirror;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.l0neman.pluginlib.util.Reflect;
import io.l0neman.pluginlib.util.reflect.mirror.throwable.MirrorException;
import io.l0neman.pluginlib.util.reflect.mirror.util.MethodHelper;

/**
 * Created by l0neman on 2019/07/18.
 */
public class MirrorConstructor<T> {
  private Constructor mConstructor;
  private Map<String, Constructor> mOverloadConstructorMap = new HashMap<>();

  public MirrorConstructor(Constructor[] overloadConstructor) {
    if (overloadConstructor.length == 1) {
      mConstructor = overloadConstructor[0];
    }

    for (Constructor constructor : overloadConstructor) {
      mOverloadConstructorMap.put(MethodHelper.getSignature("c",
          constructor.getParameterTypes()), constructor);
    }
  }

  public T newInstance(Object... args) throws MirrorException {
    if (mConstructor == null) {
      return newInstanceOverload((String[]) null, args);
    }

    try {
      return Reflect.with(mConstructor).create(args);
    } catch (Reflect.ReflectException e) {
      throw new MirrorException("newInstance", e);
    }
  }

  public T newInstanceOverload(String[] parameterTypes, Object... args) throws MirrorException {
    return newInstanceOverload(MethodHelper.getParameterTypes(parameterTypes), args);
  }

  public T newInstanceOverload(Class[] parameterTypes, Object... args) throws MirrorException {
    if (parameterTypes == null) {
      parameterTypes = new Class[0];
    }

    Constructor constructor = mOverloadConstructorMap.get(MethodHelper.getSignature("c", parameterTypes));

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
