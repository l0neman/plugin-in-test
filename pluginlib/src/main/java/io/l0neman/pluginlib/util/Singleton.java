package io.l0neman.pluginlib.util;

public abstract class Singleton<T> {

  private T mInstance;

  protected abstract T create();

  public T get() {
    if (mInstance == null) {
      mInstance = create();
    }

    return mInstance;
  }
}
