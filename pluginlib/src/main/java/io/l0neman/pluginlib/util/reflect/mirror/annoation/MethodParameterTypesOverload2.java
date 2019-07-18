package io.l0neman.pluginlib.util.reflect.mirror.annoation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MethodParameterTypesOverload2 {
  String[] overload0();

  String[] overload1();

  String[] overload2();
}
