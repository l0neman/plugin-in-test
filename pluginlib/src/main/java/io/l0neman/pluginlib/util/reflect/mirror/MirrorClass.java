package io.l0neman.pluginlib.util.reflect.mirror;

import androidx.collection.ArrayMap;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.l0neman.pluginlib.util.Reflect;
import io.l0neman.pluginlib.util.reflect.mirror.annoation.TargetMirrorClass;
import io.l0neman.pluginlib.util.reflect.mirror.annoation.TargetMirrorClassName;
import io.l0neman.pluginlib.util.reflect.mirror.throwable.MirrorException;
import io.l0neman.pluginlib.util.reflect.mirror.util.MirrorMethodHelper;

/**
 * Created by l0neman on 2019/07/19.
 * <p>
 * Target mirror class.
 * <p>
 */
@SuppressWarnings({"JavadocReference"})
public class MirrorClass<M> {

  // 0 args placeholder.
  private static Class<?>[] ARGS_PLACEHOLDER = new Class[0];

  // target mirror object;
  M mTargetMirrorObject;

  // records for target changed.
  List<MirrorMethod> mMethodRecords = new LinkedList<>();
  List<MirrorField> mFieldRecords = new LinkedList<>();

  // caches for `invoke` and `construct` (key - method signature).
  private Map<String, Constructor> mLazyConstructors = new ArrayMap<>();
  private Map<String, Method> mLazyMethods = new ArrayMap<>();

  // caches for `invokeStatic`;
  private static Map<Class<?>, Map<String, Method>> sLazyMethods = new ArrayMap<>();

  // mirror helper:

  public static class InvokeRuntimeException extends RuntimeException {

    public InvokeRuntimeException(Throwable cause) { super(cause); }
  }

  public M getTargetMirrorObject() {
    return mTargetMirrorObject;
  }

  public void setTargetMirrorObject(M mTargetMirrorObject) {
    this.mTargetMirrorObject = mTargetMirrorObject;

    notifyTargetChanged();
  }

  /**
   * wrapp {@link #setTargetMirrorObject(Object)}
   */
  public <T extends MirrorClass<M>> T attach(M mTargetMirrorObject) {
    setTargetMirrorObject(mTargetMirrorObject);
    // noinspection unchecked
    return (T) this;
  }

  private void notifyTargetChanged() {
    for (MirrorMethod method : mMethodRecords) {
      method.setObject(mTargetMirrorObject);
    }

    for (MirrorField field : mFieldRecords) {
      field.setObject(mTargetMirrorObject);
    }
  }

  protected static Class<?>[] $(Class<?>... parameterTypes) {
    return parameterTypes;
  }

  protected static Class<?>[] $(String... parameterTypes) {
    return MirrorMethodHelper.getParameterTypes(parameterTypes);
  }

  /**
   * for no parameter constructor.
   *
   * @see #construct(Class, Class[], Object...)
   */
  protected void construct(Class<? extends MirrorClass> mirrorClass) throws InvokeRuntimeException {
    construct(mirrorClass, (Class<?>[]) null);
  }

  /**
   * When you need to map the constructor of the target mirror class,
   * you need to call this method to construct and map the target object.
   *
   * @param mirrorClass    mirror class.
   * @param parameterTypes constructor parameterTypes.
   * @param args           constructor parameters.
   * @throws InvokeRuntimeException otherwise.
   */
  protected void construct(Class<? extends MirrorClass> mirrorClass, Class<?>[] parameterTypes,
                           Object... args) throws InvokeRuntimeException {

    if (parameterTypes == null) {
      parameterTypes = ARGS_PLACEHOLDER;
    }

    final String signature = MirrorMethodHelper.getSignature("c", parameterTypes);
    Constructor constructor = mLazyConstructors.get(signature);

    if (constructor == null) {
      Class<?> targetMirrorClass;
      try {
        targetMirrorClass = getTargetMirrorClass(mirrorClass);
      } catch (MirrorException e) {
        throw new RuntimeException(e);
      }

      try {
        constructor = Reflect.with(targetMirrorClass).creator()
            .parameterTypes(parameterTypes).getConstructor();
      } catch (Reflect.ReflectException e) {
        throw new RuntimeException(e);
      }

      mLazyConstructors.put(signature, constructor);
    }

    Object targetMirrorObject;
    try {
      targetMirrorObject = Reflect.with(constructor).create(args);
    } catch (Reflect.ReflectException e) {
      if (e.getCause() instanceof InvocationTargetException) {
        throw new InvokeRuntimeException(e.getCause());
      }

      throw new RuntimeException(e);
    }

    try {
      // noinspection unchecked
      map(targetMirrorObject, (Class<MirrorClass>) this.getClass(), this);
    } catch (MirrorException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Use a string to represent the parameter types.
   *
   * @see #construct(Class, Class[], Object...)
   */
  protected void construct(Class<? extends MirrorClass> mirrorClass, String[] parameterTypes,
                           Object... args) throws InvokeRuntimeException {
    construct(mirrorClass, $(parameterTypes), args);
  }

  /**
   * for no parameter method.
   *
   * @see #invoke(String, Class[], Object...)
   */
  protected <T> T invoke(String methodName) throws InvokeRuntimeException {
    return invoke(methodName, (Class<?>[]) null);
  }

  /**
   * You need to call this method to provide method information when you need to
   * map an instance method of a target mirror class.
   *
   * @param methodName     method name.
   * @param parameterTypes method parameterTypes.
   * @param args           method parameters.
   * @return The return value of the target mapping method
   */
  protected <T> T invoke(String methodName, Class<?>[] parameterTypes, Object... args)
      throws InvokeRuntimeException {
    if (parameterTypes == null) {
      parameterTypes = ARGS_PLACEHOLDER;
    }

    final String signature = MirrorMethodHelper.getSignature(methodName, parameterTypes);
    Method method = mLazyMethods.get(signature);

    if (method == null) {
      try {
        method = Reflect.with(mTargetMirrorObject).invoker().method(methodName)
            .paramsType(parameterTypes).getMethod();
      } catch (Reflect.ReflectException e) {
        throw new RuntimeException(e);
      }

      mLazyMethods.put(signature, method);
    }

    try {
      return Reflect.with(method).targetObject(mTargetMirrorObject).invoke(args);
    } catch (Reflect.ReflectException e) {
      if (e.getCause() instanceof InvocationTargetException) {
        throw new InvokeRuntimeException(e.getCause());
      }

      throw new RuntimeException(e);
    }
  }

  /**
   * Use a string to represent the parameter types.
   *
   * @see #invoke(String, Class[], Object...)
   */
  protected <T> T invoke(String methodName, String[] parameterTypes, Object... args)
      throws InvokeRuntimeException {
    return invoke(methodName, $(parameterTypes), args);
  }

  /**
   * for no parameter static method.
   *
   * @see #invokeStatic(Class, String, Class[], Object...)
   */
  protected static <T> T invokeStatic(Class<? extends MirrorClass> mirrorClass, String methodName)
      throws InvokeRuntimeException {

    return invokeStatic(mirrorClass, methodName, (Class<?>[]) null);
  }

  /**
   * You need to call this method to provide method information when you need to
   * map a static method of the target mirror class.
   *
   * @param mirrorClass    mirror class
   * @param methodName     method name.
   * @param parameterTypes method parameterTypes.
   * @param args           method parameters.
   * @return The return value of the target mapping method
   */
  protected static <T> T invokeStatic(Class<? extends MirrorClass> mirrorClass, String methodName,
                                      Class<?>[] parameterTypes, Object... args)
      throws InvokeRuntimeException {

    if (parameterTypes == null) {
      parameterTypes = ARGS_PLACEHOLDER;
    }

    Class<?> targetMirrorClass = null;
    try {
      targetMirrorClass = getTargetMirrorClass(mirrorClass);
    } catch (MirrorException e) {
      throw new RuntimeException(e);
    }

    final String signature = MirrorMethodHelper.getSignature(methodName, parameterTypes);
    Map<String, Method> methodMap = sLazyMethods.get(mirrorClass);

    if (methodMap == null) {
      methodMap = new ArrayMap<>();
      sLazyMethods.put(targetMirrorClass, methodMap);
    }

    Method method = methodMap.get(signature);
    if (method == null) {
      try {
        method = Reflect.with(targetMirrorClass).invoker().method(methodName)
            .paramsType(parameterTypes).getMethod();
      } catch (Reflect.ReflectException e) {
        throw new RuntimeException(e);
      }

      methodMap.put(signature, method);
    }

    try {
      return Reflect.with(method).invoke(args);
    } catch (Reflect.ReflectException e) {
      if (e.getCause() instanceof InvocationTargetException) {
        throw new InvokeRuntimeException(e.getCause());
      }

      throw new RuntimeException(e);
    }
  }

  /**
   * Use a string to represent the parameter types.
   *
   * @see #invokeStatic(Class, String, Class[], Object...)
   */
  protected static <T> T invokeStatic(Class<? extends MirrorClass> mirrorClass, String methodName,
                                      String[] parameterTypes, Object... args) {
    return invokeStatic(mirrorClass, methodName, $(parameterTypes), args);
  }

  // mirror annotation utils:

  // get mirror class from annotation.
  private static Class<?> getTargetMirrorClass(Class<? extends MirrorClass> mirrorClass)
      throws MirrorException {

    final TargetMirrorClassName mirrorClassName = mirrorClass.getAnnotation(TargetMirrorClassName.class);

    if (mirrorClassName != null) {
      return Reflect.with(mirrorClassName.value()).getClazz();
    }

    final TargetMirrorClass targetMirrorClass = mirrorClass.getAnnotation(TargetMirrorClass.class);

    if (targetMirrorClass != null) {
      return targetMirrorClass.value();
    }

    throw new MirrorException("not get mirror class name, please add annotation. for mirror class: " +
        mirrorClass.getSimpleName());
  }

  // mirror utils:

  // attach target mirror object to mirror object.
  private static <T extends MirrorClass> void map(Object targetMirrorObject, Class<T> mirrorClass,
                                                  T mirrorObject) throws MirrorException {
    Class<?> targetMirrorClass = getTargetMirrorClass(mirrorClass);

    try {
      // mapped by constructor.
      mirrorObject.mTargetMirrorObject = targetMirrorObject;

      for (Field field : mirrorClass.getDeclaredFields()) {
        final Class<?> fieldType = field.getType();
        final boolean isStatic = Modifier.isStatic(field.getModifiers());

        // skip constants.
        if (isStatic && Modifier.isFinal(field.getModifiers())) {
          continue;
        }

        // for mirror constructor.
        if (MirrorConstructor.class.isAssignableFrom(fieldType)) {
          Constructor[] targetMirrorOverloadConstructor;

          final Class<?>[][] mompt = MirrorMethodHelper.getMirrorOverloadMethodParameterTypes(field);

          targetMirrorOverloadConstructor = new Constructor[mompt.length];

          for (int i = 0; i < mompt.length; i++) {
            final Constructor mirrorConstructor = Reflect.with(targetMirrorClass).creator()
                .parameterTypes(mompt[i]).getConstructor();

            targetMirrorOverloadConstructor[i] = mirrorConstructor;
          }

          if (isStatic) {
            field.set(null, new MirrorConstructor(targetMirrorOverloadConstructor));
          } else {
            field.set(mirrorObject, new MirrorConstructor(targetMirrorOverloadConstructor));
          }

          continue; // end mirror constructor.
        }

        // for mirror field.
        if (MirrorField.class.isAssignableFrom(fieldType)) {
          Field targetMirrorField;

          targetMirrorField = Reflect.with(targetMirrorClass).injector().field(field.getName())
              .getField();

          if (isStatic) {
            field.set(null, new MirrorField(targetMirrorField));
          } else {
            final MirrorField mirrorField = new MirrorField(targetMirrorObject, targetMirrorField);
            field.set(mirrorObject, mirrorField);

            // noinspection unchecked
            mirrorObject.mFieldRecords.add(mirrorField);
          }

          continue; // end mirror field.
        }

        // for mirror method.
        if (MirrorMethod.class.isAssignableFrom(fieldType)) {
          Method[] targetMirrorOverloadMethod;

          final Class<?>[][] mompt = MirrorMethodHelper.getMirrorOverloadMethodParameterTypes(field);

          targetMirrorOverloadMethod = new Method[mompt.length];

          for (int i = 0; i < mompt.length; i++) {
            final Method mirrorMethod = Reflect.with(targetMirrorClass).invoker()
                .method(field.getName())
                .paramsType(mompt[i]).getMethod();

            targetMirrorOverloadMethod[i] = mirrorMethod;
          }

          if (isStatic) {
            field.set(null, new MirrorMethod(targetMirrorOverloadMethod));
          } else {
            final MirrorMethod mirrorMethod = new MirrorMethod(targetMirrorObject, targetMirrorOverloadMethod);
            field.set(mirrorObject, mirrorMethod);

            // noinspection unchecked
            mirrorObject.mMethodRecords.add(mirrorMethod);
          }

          continue; // end mirror method.
        }

        // for MirrorClass.
        if (MirrorClass.class.isAssignableFrom(fieldType)) {

          final Object targetMirrorField = Reflect.with(targetMirrorClass).injector()
              .field(field.getName()).targetObject(targetMirrorObject).get();

          // noinspection unchecked
          final MirrorClass mapClass = MirrorClass.map(targetMirrorField,
              (Class<? extends MirrorClass>) fieldType);

          if (isStatic) {
            field.set(null, mapClass);
          } else {
            field.set(mirrorObject, mapClass);
          }

        }
      }
    } catch (Exception e) {
      throw new MirrorException("map", e);
    }
  }

  /**
   * ignore exceptions.
   *
   * @see #map(Class)
   */
  public static <T extends MirrorClass> T mapQuiet(Class<T> mirrorClass) {
    try {
      return map(mirrorClass);
    } catch (MirrorException e) {
      throw new RuntimeException("mirror for " + mirrorClass, e);
    }
  }

  /**
   * ignore exceptions.
   *
   * @see #map(Object, Class)
   */
  public static <T extends MirrorClass> T mapQuiet(Object targetMirrorObject, Class<T> mirrorClass) {
    try {
      return map(targetMirrorObject, mirrorClass);
    } catch (MirrorException e) {
      throw new RuntimeException("mirror for " + mirrorClass, e);
    }
  }

  public static <T extends MirrorClass> T map(Class<T> mirrorClass) throws MirrorException {
    return map(null, mirrorClass);
  }

  public static <T extends MirrorClass> T map(Object targetMirrorObject, Class<T> mirrorClass)
      throws MirrorException {

    final T mirrorObject;
    try {
      mirrorObject = mirrorClass.newInstance();
    } catch (Exception e) {
      throw new MirrorException("Need to provide a non-parametric constructor", e);
    }

    map(targetMirrorObject, mirrorClass, mirrorObject);

    return mirrorObject;
  }

  /**
   * Use ThreadLocal to ensure thread safety.
   *
   * @see #mapQuiet(Class)
   */
  public static <T extends MirrorClass> T mapQuietThreadSafe(final Class<T> mirrorClass) {
    return new ThreadLocal<T>() {
      @Override
      protected T initialValue() { return mapQuiet(mirrorClass); }
    }.get();
  }

  /**
   * Use ThreadLocal to ensure thread safety.
   *
   * @see #mapQuietThreadSafe(Object, Class)
   */
  public static <T extends MirrorClass> T mapQuietThreadSafe(final Object targetMirrorObject,
                                                             final Class<T> mirrorClass) {
    return new ThreadLocal<T>() {
      @Override
      protected T initialValue() { return mapQuiet(targetMirrorObject, mirrorClass); }
    }.get();
  }
}
