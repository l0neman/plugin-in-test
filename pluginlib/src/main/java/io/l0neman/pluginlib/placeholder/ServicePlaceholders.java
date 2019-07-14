package io.l0neman.pluginlib.placeholder;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by l0neman on 2019/07/14.
 */
public class ServicePlaceholders {

  public static class Service0 extends Service {

    @Override public IBinder onBind(Intent intent) {
      return null;
    }
  }
}
