package io.l0neman.pluginlib.placeholder;

import android.app.Activity;
import android.app.Service;

import androidx.collection.ArrayMap;

import java.util.Map;

import io.l0neman.pluginlib.util.Singleton;

public class PlaceholderManager {

  private static final int ACTIVITY_MAX = 9;
  private static final int SERVICE_MAX = 9;

  private int mActivityIndex;
  private int mServiceIndex;

  private Map<String, Class<? extends Activity>> mActivityMap = new ArrayMap<>();
  private Map<String, Class<? extends Service>> mServiceMap = new ArrayMap<>();

  private Class[] mActivityClasses = {
      ActivityPlaceholders.Activity0.class,
      ActivityPlaceholders.Activity1.class,
      ActivityPlaceholders.Activity2.class,
      ActivityPlaceholders.Activity3.class,
      ActivityPlaceholders.Activity4.class,
      ActivityPlaceholders.Activity5.class,
      ActivityPlaceholders.Activity6.class,
      ActivityPlaceholders.Activity7.class,
      ActivityPlaceholders.Activity8.class,
      ActivityPlaceholders.Activity9.class,
  };

  private Class[] mServiceClasses = {
      ServicePlaceholders.Service0.class,
      ServicePlaceholders.Service1.class,
      ServicePlaceholders.Service2.class,
      ServicePlaceholders.Service3.class,
      ServicePlaceholders.Service4.class,
      ServicePlaceholders.Service5.class,
      ServicePlaceholders.Service6.class,
      ServicePlaceholders.Service7.class,
      ServicePlaceholders.Service8.class,
      ServicePlaceholders.Service9.class,
  };

  private Map<String, String> mActivityKeyMap = new ArrayMap<>();
  private Map<String, String> mServiceKeyMap = new ArrayMap<>();

  private static Singleton<PlaceholderManager> sInstance = new Singleton<PlaceholderManager>() {
    @Override protected PlaceholderManager create() {
      return new PlaceholderManager();
    }
  };

  public static PlaceholderManager getInstance() {
    return sInstance.get();
  }

  private PlaceholderManager() {}

  public String queryKeyActivityName(String activityName) {
    return mActivityKeyMap.get(activityName);
  }

  public String queryKeyServiceName(String serviceName) {
    return mServiceKeyMap.get(serviceName);
  }

  public Class<? extends Activity> applyActivity(String keyActivityName) {
    Class<? extends Activity> aClass = mActivityMap.get(keyActivityName);
    if (aClass != null) {
      return aClass;
    }

    if (mActivityIndex == ACTIVITY_MAX) {
      throw new IndexOutOfBoundsException("activity max.");
    }

    // noinspection unchecked
    Class<? extends Activity> current = mActivityClasses[mActivityIndex];
    mActivityMap.put(keyActivityName, current);
    mActivityKeyMap.put(current.getName(), keyActivityName);
    mActivityIndex++;
    return current;
  }

  public Class<? extends Service> applyService(String keyServiceName) {
    Class<? extends Service> sClass = mServiceMap.get(keyServiceName);
    if (sClass != null) {
      return sClass;
    }

    if (mServiceIndex == SERVICE_MAX) {
      throw new IndexOutOfBoundsException("activity max.");
    }

    // noinspection unchecked
    Class<? extends Service> current = mServiceClasses[mServiceIndex];
    mServiceMap.put(keyServiceName, current);
    mActivityKeyMap.put(current.getName(), keyServiceName);
    mServiceIndex++;
    return current;
  }
}
