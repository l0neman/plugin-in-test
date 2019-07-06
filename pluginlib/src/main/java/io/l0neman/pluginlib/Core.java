package io.l0neman.pluginlib;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import io.l0neman.pluginlib.provider.android.app.ActivityManagerNative;
import io.l0neman.pluginlib.provider.android.app.ActivityThread;
import io.l0neman.pluginlib.support.PLLooger;
import io.l0neman.pluginlib.util.Reflect;
import io.l0neman.pluginlib.util.reflect.ReflectClass;

public final class Core {

  private static final String TAG = "PLLogger";

  // hook Android L.
  private static class ActivityManagerProxy implements InvocationHandler {

    private final Object mActivityManager;

    ActivityManagerProxy(Object mActivityManager) {
      this.mActivityManager = mActivityManager;
    }

    @Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      PLLooger.d(TAG, "ActivityManager: " + method.getName());
      return method.invoke(mActivityManager, args);
    }
  }

  private static void hookActivityManager() {
    try {
      /*
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
      // */

      ActivityManagerNative.mirrorStaticMembers(ActivityManagerNative.class);
      Object mInstance = ActivityManagerNative.gDefault.mInstance.getValue();

      Object newAM = Proxy.newProxyInstance(
          Thread.currentThread().getContextClassLoader(),
          new Class<?>[]{Reflect.with("android.app.IActivityManager").getProviderClass()},
          new ActivityManagerProxy(mInstance)
      );

      ActivityManagerNative.gDefault.mInstance.setValue(newAM);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static final class HCallbackProxy implements Handler.Callback {

    private final Handler mH;

    public HCallbackProxy(Handler mH) {
      this.mH = mH;
    }

    @Override public boolean handleMessage(Message msg) {

      switch (msg.what) {
      case ActivityThread.H.LAUNCH_ACTIVITY:
        PLLooger.d(TAG, "LAUNCH_ACTIVITY: " + msg.obj);
        break;
      }

      mH.handleMessage(msg);
      return true;
    }
  }

  public static void hookActivityThreadH() {
    try {
      /*
      Object sCurrentActivityThread = Reflect.with("android.app.ActivityThread").injector()
          .field("sCurrentActivityThread")
          .get();

      Handler mH = Reflect.with("android.app.ActivityThread").injector()
          .targetObject(sCurrentActivityThread)
          .field("mH")
          .get();
      // */

      ActivityThread.mirrorStaticMembers(ActivityThread.class);
      Object sCurrentActivityThread = ActivityThread.sCurrentActivityThread.getValue();

      ActivityThread activityThread = ReflectClass.mirror(sCurrentActivityThread,
          ActivityThread.class);

      Handler mH = activityThread.mH.getValue();

      Reflect.with(Handler.class).injector()
          .targetObject(mH)
          .field("mCallback")
          .set(new HCallbackProxy(mH));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void appAttachBaseContext(Context context) {
    hookActivityManager();
    hookActivityThreadH();
  }
}
