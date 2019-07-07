package io.l0neman.pluginlib.util.reflect.mirror;


import io.l0neman.pluginlib.util.Reflect;
import io.l0neman.pluginlib.util.reflect.mirror.throwable.MirrorException;

/**
 * Created by l0neman on 2019/07/06.
 */
public class MirrorField<T> {

  private Class<?> mClass;
  private Object mObject;
  private String mName;

  // for object's field.
  public MirrorField(Class<?> mClass, Object mObject, String mName) {
    this.mClass = mClass;
    this.mObject = mObject;
    this.mName = mName;
  }

  // for class's static field.
  public MirrorField(Class<?> mClass, String mName) {
    this.mClass = mClass;
    this.mName = mName;
  }

  public T getValue() throws MirrorException {
    try {
      return Reflect.with(mObject == null ? mClass : mObject).injector()
          .field(mName)
          .get();
    } catch (Exception e) {
      throw new MirrorException("get field value", e);
    }
  }

  public void setValue(Object value) throws MirrorException {
    try {
      Reflect.with(mObject == null ? mClass : mObject).injector()
          .field(mName)
          .set(value);
    } catch (Exception e) {
      throw new MirrorException("get field value", e);
    }
  }
}
