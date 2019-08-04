package io.l0neman.pluginlib.util.reflect.mirror.util;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import io.l0neman.pluginlib.util.reflect.mirror.throwable.MirrorException;

/**
 * Created by l0neman on 2019/08/04.
 */
public class ActionChecker {

  public static void checkMethod(Method method, Object action) throws MirrorException {
    checkAction(method, action, "method");
  }

  public static void checkField(Field field, Object action) throws MirrorException {
    checkAction(field, action, "field");
  }

  private static void checkAction(Member method, Object action, String logKey)
      throws MirrorException {

    if (Modifier.isStatic(method.getModifiers())) {
      if (action != null) {
        throw new MirrorException("Static " + logKey + " cannot specify an object");
      }
    } else {
      if (action == null) {
        throw new MirrorException("Instance " + logKey + " missing call object");
      }
    }
  }
}
