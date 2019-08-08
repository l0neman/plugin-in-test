package io.l0neman.pluginlib.util.reflect.mirror.util;

import androidx.collection.ArrayMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import io.l0neman.pluginlib.util.Reflect;
import io.l0neman.pluginlib.util.reflect.mirror.annoation.MethodParameterClasses;
import io.l0neman.pluginlib.util.reflect.mirror.annoation.MethodParameterClassesOverload;
import io.l0neman.pluginlib.util.reflect.mirror.annoation.MethodParameterClassesOverload2;
import io.l0neman.pluginlib.util.reflect.mirror.annoation.MethodParameterTypes;
import io.l0neman.pluginlib.util.reflect.mirror.annoation.MethodParameterTypesOverload;
import io.l0neman.pluginlib.util.reflect.mirror.annoation.MethodParameterTypesOverload2;

@SuppressWarnings("JavadocReference")
public class MirrorMethodHelper {

  /**
   * 通过方法名称和参数类型生成签名。
   * <p>
   * Example:
   * testMethod(java.lang.String, int, boolean); => testMethod(Ljava\lang\String;IZ)
   *
   * @param prefix         签名前缀。
   * @param parameterTypes 参数类型。
   * @return 方法签名，用于缓存 key。
   */
  public static String getSignature(String prefix, Class<?>... parameterTypes) {
    StringBuilder methodSignature = new StringBuilder(prefix + "(");

    for (Class parameterType : parameterTypes) {
      methodSignature.append(typeSignature(parameterType));
    }

    return methodSignature.append(')').toString();
  }

  // table drive.
  private static final Map<Class<?>, String> TYPE_SIGNATURE = new ArrayMap<Class<?>, String>() {
    {
      put(boolean.class, "Z");
      put(byte.class, "B");
      put(char.class, "C");
      put(short.class, "S");
      put(int.class, "I");
      put(long.class, "J");
      put(float.class, "F");
      put(double.class, "D");
    }
  };

  private static String typeSignature(Class<?> clazz) {
    String ts = TYPE_SIGNATURE.get(clazz);
    if (ts != null) {
      // primitive type.
      return ts;
    }

    if (clazz.isArray()) {
      // array type.
      return clazz.getName().replace('.', '\\');
    }

    // reference type.
    ts = "L" + clazz.getName().replace('.', '\\') + ';';

    return ts;
  }

  /**
   * @param method method.
   * @return method signature.
   *
   * @see #getSignature(String, Class[])
   */
  public static String getSignature(Method method) {
    return getSignature(method.getName(), method.getParameterTypes());
  }

  /**
   * covert string[] to class[].
   *
   * @param parameterClasses parameter class names.
   * @return class[]
   */
  public static Class<?>[] getParameterTypes(String[] parameterClasses) {
    Class<?>[] parameterTypes = new Class[parameterClasses.length];
    for (int i = 0; i < parameterTypes.length; i++) {
      parameterTypes[i] = forName(parameterClasses[i]);
    }

    return parameterTypes;
  }

  // class for name, support primitive types.
  private static Class<?> forName(String className) {
    if ("byte".equals(className)) {
      return byte.class;
    }

    if ("char".equals(className)) {
      return char.class;
    }

    if ("short".equals(className)) {
      return short.class;
    }

    if ("int".equals(className)) {
      return int.class;
    }

    if ("float".equals(className)) {
      return float.class;
    }

    if ("double".equals(className)) {
      return double.class;
    }

    if ("long".equals(className)) {
      return long.class;
    }

    if ("boolean".equals(className)) {
      return boolean.class;
    }

    return Reflect.with(className).getClazz();
  }

  /**
   * get all overload method parameter types from annotation.
   */
  public static Class<?>[][] getMirrorOverloadMethodParameterTypes(Field mirrorMethod) {

    final MethodParameterTypes mmpt = mirrorMethod.getAnnotation(MethodParameterTypes.class);

    if (mmpt != null) {
      return new Class<?>[][]{mmpt.value()};
    }

    final MethodParameterClasses mpts = mirrorMethod.getAnnotation(MethodParameterClasses.class);

    if (mpts != null) {
      return new Class[][]{getParameterTypes(mpts.value())};
    }

    final MethodParameterTypesOverload mmpto1 = mirrorMethod.getAnnotation(
        MethodParameterTypesOverload.class);

    if (mmpto1 != null) {
      Class<?>[][] parameterTypesArray = new Class[2][];

      parameterTypesArray[0] = mmpto1.overload0();
      parameterTypesArray[1] = mmpto1.overload1();

      return parameterTypesArray;
    }

    final MethodParameterTypesOverload2 mmpto2 = mirrorMethod.getAnnotation(
        MethodParameterTypesOverload2.class);

    if (mmpto2 != null) {
      Class<?>[][] parameterTypesArray = new Class[3][];

      parameterTypesArray[0] = mmpto2.overload0();
      parameterTypesArray[1] = mmpto2.overload1();
      parameterTypesArray[2] = mmpto2.overload2();

      return parameterTypesArray;
    }

    final MethodParameterClassesOverload mpto1 = mirrorMethod.getAnnotation(
        MethodParameterClassesOverload.class);

    if (mpto1 != null) {
      Class<?>[][] parameterTypesArray = new Class[2][];

      parameterTypesArray[0] = getParameterTypes(mpto1.overload0());
      parameterTypesArray[1] = getParameterTypes(mpto1.overload1());

      return parameterTypesArray;
    }

    final MethodParameterClassesOverload2 mpto2 = mirrorMethod.getAnnotation(
        MethodParameterClassesOverload2.class);

    if (mpto2 != null) {
      Class<?>[][] parameterTypesArray = new Class[3][];

      parameterTypesArray[0] = getParameterTypes(mpto2.overload0());
      parameterTypesArray[1] = getParameterTypes(mpto2.overload1());
      parameterTypesArray[2] = getParameterTypes(mpto2.overload2());

      return parameterTypesArray;
    }

    /*
    throw new MirrorException("not found method parameter types, please add " +
        "MirrorMethodParameterTypes annotation. mirror method: " + mirrorMethod.getName());
    // */

    return new Class[][]{new Class[0]};
  }
}
