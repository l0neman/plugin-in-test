package io.l0neman.pluginlib.util.reflect.mirror;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.l0neman.pluginlib.util.Reflect;
import io.l0neman.pluginlib.util.reflect.mirror.throwable.MirrorException;
import io.l0neman.pluginlib.util.reflect.mirror.util.MethodHelper;

/**
 * Created by l0neman on 2019/07/06.
 */
public class MirrorMethod<T> {

  private Object mObject;
  private Method mMethod;
  private Map<String, Method> mOverloadMethodMap = new HashMap<>();

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
      mMethod = overloadMethod[0];
    }

    for (Method method : overloadMethod) {
      mOverloadMethodMap.put(MethodHelper.getSignature("o", method.getParameterTypes()), method);
    }
  }

  public void setObject(Object mObject) {
    this.mObject = mObject;
  }

  public T invoke(Object... args) throws MirrorException {
    if (mMethod == null) {
      return invokeOverload((String[]) null, args);
    }

    try {
      return Reflect.with(mMethod).targetObject(mObject).invoke(args);
    } catch (Exception e) {
      throw new MirrorException(e);
    }
  }

  public T invokeOverload(String[] parameterTypes, Object... args) throws MirrorException {
    return invokeOverload(MethodHelper.getParameterTypes(parameterTypes), args);
  }

  public T invokeOverload(Class[] parameterTypes, Object... args) throws MirrorException {
    if (parameterTypes == null) {
      parameterTypes = new Class[0];
    }

    Method method = mOverloadMethodMap.get(MethodHelper.getSignature("o", parameterTypes));
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
