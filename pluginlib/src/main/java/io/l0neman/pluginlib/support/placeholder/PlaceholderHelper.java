package io.l0neman.pluginlib.support.placeholder;

import androidx.collection.ArrayMap;

import java.util.Map;

import io.l0neman.pluginlib.util.Singleton;

public class PlaceholderHelper {

  private static final int ACTIVITY_MAX = 9;
  private static final int SERVICE_MAX = 9;

  private int mActivityIndex;
  private int mServiceIndex;

  private Map<String, String> mActivityMap = new ArrayMap<>();
  private Map<String, String> mServiceMap = new ArrayMap<>();

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

  private static Singleton<PlaceholderHelper> sInstance = new Singleton<PlaceholderHelper>() {
    @Override protected PlaceholderHelper create() {
      return new PlaceholderHelper();
    }
  };

  public static PlaceholderHelper getInstance() {
    return sInstance.get();
  }

  private PlaceholderHelper() {}

  public String queryKeyActivity(String appliedActivityName) {
    return mActivityKeyMap.get(appliedActivityName);
  }

  public String queryKeyService(String appliedServiceName) {
    return mServiceKeyMap.get(appliedServiceName);
  }

  public String applyActivity(String keyActivityName) {
    String aClassName = mActivityMap.get(keyActivityName);
    if (aClassName != null) {
      return aClassName;
    }

    if (mActivityIndex == ACTIVITY_MAX) {
      throw new IndexOutOfBoundsException("activity max.");
    }

    String current = mActivityClasses[mActivityIndex].getName();
    mActivityMap.put(keyActivityName, current);
    mActivityKeyMap.put(current, keyActivityName);
    mActivityIndex++;
    return current;
  }

  public String applyService(String keyServiceName) {
    String sClass = mServiceMap.get(keyServiceName);
    if (sClass != null) {
      return sClass;
    }

    if (mServiceIndex == SERVICE_MAX) {
      throw new IndexOutOfBoundsException("activity max.");
    }

    String current = mServiceClasses[mServiceIndex].getName();
    mServiceMap.put(keyServiceName, current);
    mServiceKeyMap.put(current, keyServiceName);
    mServiceIndex++;
    return current;
  }
}
