package io.l0neman.pluginlib.util.reflect.mirror;

import androidx.collection.ArrayMap;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import io.l0neman.pluginlib.util.Reflect;
import io.l0neman.pluginlib.util.reflect.mirror.throwable.MirrorException;
import io.l0neman.pluginlib.util.reflect.mirror.util.MethodInfo;

/**
 * Created by l0neman on 2019/07/06.
 */
public class MirrorMethod<T> {

  private Object mObject;
  private Method mMethod;
  private Map<String, Method> mOverloadMethodMap;

  // for object method.
  public MirrorMethod(Object mObject, Method[] overloadMethod) {
    this.mObject = mObject;
    process(overloadMethod);
  }

  // for static method.
  public MirrorMethod(Method[] overloadMethod) {
    process(overloadMethod);
  }

  private void process(Method[] overloadMethod) {
    if (overloadMethod.length == 1) {
      this.mMethod = overloadMethod[0];
      return;
    }

    mOverloadMethodMap = new ArrayMap<>();

    for (Method method : overloadMethod) {
      mOverloadMethodMap.put(
          MethodInfo.getSignature("", method.getParameterTypes()),
          method);
    }
  }

  public void setObject(Object mObject) {
    this.mObject = mObject;
  }

  public boolean isOverload() {
    return mOverloadMethodMap != null;
  }

  public T invoke(Object... args) throws MirrorException {
    try {
      return Reflect.with(mMethod).targetObject(mObject).invoke(args);
    } catch (Exception e) {
      throw new MirrorException(e);
    }
  }

  public T invoke(Class<?>[] parameterTypes, Object... args) throws MirrorException {
    Method method = mOverloadMethodMap.get(MethodInfo.getSignature("", parameterTypes));
    if (method == null) {
      throw new MirrorException("not found method: " + Arrays.toString(parameterTypes));
    }

    try {
      return Reflect.with(method).targetObject(mObject).invoke(args);
    } catch (Exception e) {
      throw new MirrorException(e);
    }
  }
}
