package io.l0neman.pluginlib.test;

import android.content.Context;
import android.content.Intent;

/**
 * Created by l0neman on 2019/07/14.
 */
public class Test {


  public static void test(Context context) {
    context.startService(new Intent(context, TestService.class));
  }

  public static void test2(Context context) {
    context.stopService(new Intent(context, TestService.class));
  }
}
