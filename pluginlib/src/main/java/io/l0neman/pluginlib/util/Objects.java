package io.l0neman.pluginlib.util;

public class Objects {
  public static <T> T requireNonNull(T obj) {
    if(obj == null) {
      throw new NullPointerException();
    }

    return obj;
  }
}
