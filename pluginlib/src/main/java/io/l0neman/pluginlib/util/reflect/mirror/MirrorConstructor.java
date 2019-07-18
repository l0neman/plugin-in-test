package io.l0neman.pluginlib.util.reflect.mirror;

import java.lang.reflect.Constructor;

import io.l0neman.pluginlib.util.Reflect;
import io.l0neman.pluginlib.util.reflect.mirror.throwable.MirrorException;

/**
 * Created by l0neman on 2019/07/18.
 */
public class MirrorConstructor<T> {
  private Constructor<T> mConstructor;

  public MirrorConstructor(Constructor<T> mConstructor) {
    this.mConstructor = mConstructor;
  }

  public T newInstance() throws MirrorException {
    try {
      return Reflect.with(mConstructor).create();
    } catch (Reflect.ReflectException e) {
      throw new MirrorException("newInstance", e);
    }
  }

  public T newInstance(Object... args) throws MirrorException {
    try {
      return Reflect.with(mConstructor).create(args);
    } catch (Reflect.ReflectException e) {
      throw new MirrorException("newInstance", e);
    }
  }
}
