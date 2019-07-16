package io.l0neman.pluginlib.placeholder;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by l0neman on 2019/07/14.
 */
public class ServicePlaceholders {

  private static abstract class BaseService extends Service {
    @Override public IBinder onBind(Intent intent) {
      return null;
    }
  }

  public static class Service0 extends BaseService {}

  public static class Service1 extends BaseService {}

  public static class Service2 extends BaseService {}

  public static class Service3 extends BaseService {}

  public static class Service4 extends BaseService {}

  public static class Service5 extends BaseService {}

  public static class Service6 extends BaseService {}

  public static class Service7 extends BaseService {}

  public static class Service8 extends BaseService {}

  public static class Service9 extends BaseService {}
}
