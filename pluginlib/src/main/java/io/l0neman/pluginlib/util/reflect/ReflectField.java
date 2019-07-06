package io.l0neman.pluginlib.util.reflect;

import io.l0neman.pluginlib.util.reflect.throwable.ReflectException;
import io.l0neman.pluginlib.util.reflect.util.Reflect;

/**
 * Created by l0neman on 2019/07/06.
 */
public class ReflectField<T> {

  private Class<?> mClass;
  private Object mObject;
  private String mName;

  // for object's field.
  public ReflectField(Class<?> mClass, Object mObject, String mName) {
    this.mClass = mClass;
    this.mObject = mObject;
    this.mName = mName;
  }

  // for class's static field.
  public ReflectField(Class<?> mClass, String mName) {
    this.mClass = mClass;
    this.mName = mName;
  }

  public T getValue() throws ReflectException {
    try {
      return Reflect.with(mClass).injector()
          .targetObject(mObject)
          .field(mName)
          .get();
    } catch (Exception e) {
      throw new ReflectException("get field value", e);
    }
  }

  public void setValue(Object value) throws ReflectException {
    try {
      Reflect.with(mClass).injector()
          .targetObject(mObject)
          .field(mName)
          .set(value);
    } catch (Exception e) {
      throw new ReflectException("get field value", e);
    }
  }
}
