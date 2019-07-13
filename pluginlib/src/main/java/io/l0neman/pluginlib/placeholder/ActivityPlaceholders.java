package io.l0neman.pluginlib.placeholder;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

/**
 * Created by l0neman on 2019/07/07.
 */
public class ActivityPlaceholders {

  public static class Activity0 extends Activity {

    @Override protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
    }
  }

  public static class Service0 extends Service {

    @Override public IBinder onBind(Intent intent) {
      return null;
    }
  }
}
