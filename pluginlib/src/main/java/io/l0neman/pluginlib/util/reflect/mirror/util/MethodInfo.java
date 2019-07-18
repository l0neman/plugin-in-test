package io.l0neman.pluginlib.util.reflect.mirror.util;

import java.lang.reflect.Method;

import io.l0neman.pluginlib.util.Reflect;

public class MethodInfo {

  /**
   * 通过方法名称和参数类型生成签名。
   * <p>
   * Example:
   * testMethod(String, int, boolean); => testMethodStinbo
   * <p>
   * 方法名和每种参数类型的前两个字符组合，（两个字符不会导致签名出现重复）。
   *
   * @param methodName     方法名称。
   * @param parameterTypes 参数类型。
   * @return 方法签名，用于缓存 key。
   */
  public static String getSignature(String methodName, Class<?>... parameterTypes) {
    StringBuilder methodSignature = new StringBuilder(methodName);

    for (Class parameterType : parameterTypes) {
      String sm = parameterType.getSimpleName();
      methodSignature.append(sm.charAt(0)).append(sm.charAt(1));
    }

    return methodSignature.toString();
  }

  /**
   * @param method method.
   * @return method signature.
   * @see #getSignature(String, Class[])
   */
  public static String getSignature(Method method) {
    return getSignature(method.getName(), method.getParameterTypes());
  }

  public static Class<?>[] getParameterTypes(String... parameterClasses) {
    Class<?>[] parameterTypes = new Class[parameterClasses.length];
    for (int i = 0; i < parameterTypes.length; i++) {
      parameterTypes[i] = Reflect.with(parameterClasses[i]).getClazz();
    }

    return parameterTypes;
  }
}
