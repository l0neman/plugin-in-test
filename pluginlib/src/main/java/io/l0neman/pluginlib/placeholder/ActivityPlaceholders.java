package io.l0neman.pluginlib.placeholder;

import android.app.Activity;
import android.os.Bundle;

import io.l0neman.pluginlib.support.PLLogger;

/**
 * Created by l0neman on 2019/07/07.
 */
public class ActivityPlaceholders {

  public static class Activity0 extends Activity {

    @Override protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      PLLogger.w("NEW_NO", "oh no no no.");
    }
  }
}
