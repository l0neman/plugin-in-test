package io.l0neman.pluginlib;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import io.l0neman.pluginlib.util.Reflect;

public final class Core {

  private static final String TAG = "PL";

  private static class ActivityManagerProxy implements InvocationHandler {

    private Object mActivityManager;

    public ActivityManagerProxy(Object mActivityManager) {
      this.mActivityManager = mActivityManager;
    }

    @Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      Log.d(TAG, "ActivityManager: " + method.getName());
      return method.invoke(mActivityManager, args);
    }
  }

  private static void hookActivityManager() {
    try {
      Object gDefault = Reflect.with("android.app.ActivityManagerNative").injector()
          .field("gDefault")
          .get();

      Object mInstance = Reflect.with("android.util.Singleton").injector()
          .targetObject(gDefault)
          .field("mInstance")
          .get();

      Object newAM = Proxy.newProxyInstance(
          Thread.currentThread().getContextClassLoader(),
          new Class<?>[]{Reflect.with("android.app.IActivityManager").getProviderClass()},
          new ActivityManagerProxy(mInstance)
      );

      Reflect.with("android.util.Singleton").injector()
          .targetObject(gDefault)
          .field("mInstance")
          .set(newAM);

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public static void test(Context context) {
    hookActivityManager();
  }

}
