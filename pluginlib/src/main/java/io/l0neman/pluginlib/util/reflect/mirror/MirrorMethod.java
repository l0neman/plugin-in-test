package io.l0neman.pluginlib.util.reflect.mirror;

import androidx.collection.ArrayMap;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import io.l0neman.pluginlib.util.reflect.Reflect;
import io.l0neman.pluginlib.util.reflect.mirror.throwable.MirrorException;
import io.l0neman.pluginlib.util.reflect.mirror.util.ActionChecker;
import io.l0neman.pluginlib.util.reflect.mirror.util.MirrorMethodHelper;

/**
 * Created by l0neman on 2019/07/06.
 * <p>
 * The mapping of the constructor of the target mirror class.
 */
@SuppressWarnings("JavadocReference")
public class MirrorMethod<T> {

  private Object mObject;
  private Method mMethod;
  private Map<String, Method> mOverloadMethodMap = new ArrayMap<>();

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
      mOverloadMethodMap.put(MirrorMethodHelper.getSignature("o", method.getParameterTypes()), method);
    }
  }

  /**
   * Set the target call object.
   *
   * @param mObject target call object.
   */
  public MirrorMethod<T> setObject(Object mObject) {
    this.mObject = mObject;
    return this;
  }

  /**
   * Method of calling a target mirror object.
   * <p>
   * for no overloaded method.
   *
   * @param args method parameters.
   * @return method return value.
   *
   * @throws MirrorException otherwise.
   */
  public T invoke(Object... args) throws MirrorException {
    if (mMethod == null) {
      return invokeOverload((Class[]) null, args);
    }

    ActionChecker.checkMethod(mMethod, mObject);

    try {
      return Reflect.with(mMethod).targetObject(mObject).invoke(args);
    } catch (Exception e) {
      throw new MirrorException(e);
    }
  }

  /**
   * @see #invokeOverload(Class[], Object...)
   */
  public T invokeOverload(String[] parameterTypes, Object... args) throws MirrorException {
    return invokeOverload(MirrorMethodHelper.getParameterTypes(parameterTypes), args);
  }

  /**
   * Method of calling a target mirror object.
   * <p>
   * for overloaded method.
   *
   * @param parameterTypes method parameter type names.
   * @param args           method parameters.
   * @return method return value.
   *
   * @throws MirrorException otherwise.
   */
  public T invokeOverload(Class[] parameterTypes, Object... args) throws MirrorException {
    if (parameterTypes == null) {
      parameterTypes = new Class[0];
    }

    Method method = mOverloadMethodMap.get(MirrorMethodHelper.getSignature("o", parameterTypes));
    if (method == null) {
      throw new MirrorException("not found method: " + Arrays.toString(parameterTypes));
    }

    ActionChecker.checkMethod(method, mObject);

    try {
      return Reflect.with(method).targetObject(mObject).invoke(args);
    } catch (Exception e) {
      throw new MirrorException(e);
    }
  }

}
