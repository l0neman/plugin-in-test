package io.l0neman.pluginlib.util.reflect.mirror;


import java.lang.reflect.Field;

import io.l0neman.pluginlib.util.reflect.Reflect;
import io.l0neman.pluginlib.util.reflect.mirror.throwable.MirrorException;
import io.l0neman.pluginlib.util.reflect.mirror.util.ActionChecker;

/**
 * Created by l0neman on 2019/07/06.
 * <p>
 * The mapping of the field of the target mirror class.
 */
public class MirrorField<T> {

  private Object mObject;
  private final Field mField;

  // for object field.
  public MirrorField(Object mObject, Field mField) {
    this.mObject = mObject;
    this.mField = mField;
  }

  // for static field.
  public MirrorField(Field mField) {
    this.mField = mField;
  }

  /**
   * Set the target call object.
   *
   * @param mObject target call object.
   */
  public MirrorField<T> setObject(Object mObject) {
    this.mObject = mObject;
    return this;
  }

  /**
   * get field value.
   *
   * @return field's value.
   * @throws MirrorException otherwise.
   */
  public T get() throws MirrorException {
    ActionChecker.checkField(mField, mObject);

    try {
      return Reflect.with(mField).targetObject(mObject).get();
    } catch (Reflect.ReflectException e) {
      throw new MirrorException("getSignature field value", e);
    }
  }

  /**
   * set field value.
   *
   * @param value new value.
   * @throws MirrorException otherwise.
   */
  public void set(T value) throws MirrorException {
    ActionChecker.checkField(mField, mObject);

    try {
      Reflect.with(mField).targetObject(mObject).set(value);
    } catch (Reflect.ReflectException e) {
      throw new MirrorException("getSignature field value", e);
    }
  }
}
