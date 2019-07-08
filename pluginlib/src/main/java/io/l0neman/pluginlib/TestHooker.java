package io.l0neman.pluginlib;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import io.l0neman.pluginlib.mirror.android.app.ActivityManagerNative;
import io.l0neman.pluginlib.mirror.android.app.ActivityThread;
import io.l0neman.pluginlib.placeholder.ActivityPlaceholders;
import io.l0neman.pluginlib.support.PLLogger;
import io.l0neman.pluginlib.util.Reflect;
import io.l0neman.pluginlib.util.reflect.mirror.MirrorClass;

/**
 * Created by l0neman on 2019/07/07.
 */
public class TestHooker {

  private static final String TAG = "TestHooker";

  // hook Android L.
  private static class ActivityManagerProxy implements InvocationHandler {

    private final Object mActivityManager;

    ActivityManagerProxy(Object mActivityManager) {
      this.mActivityManager = mActivityManager;
    }

    @Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      PLLogger.d(TAG, "ActivityManager: " + method.getName());

      if ("startActivity".equals(method.getName())) {
        Intent raw = null;
        int i = 0;
        for (Object arg : args) {
          if (arg instanceof Intent) {
            raw = (Intent) arg;
            break;
          }

          i++;
        }

        Intent newIntent = new Intent();
        newIntent.putExtra("sssssssss", raw);
        newIntent.setComponent(new ComponentName(
            "io.l0neman.pluginlib", ActivityPlaceholders.Activity0.class.getName()
        ));

        args[i] = new Intent();
        return method.invoke(mActivityManager, args);
      }

      return method.invoke(mActivityManager, args);
    }
  }

  private static void hookActivityManager() {
    try {
      ActivityManagerNative.mirror(ActivityManagerNative.class);
      Object mInstance = ActivityManagerNative.gDefault.mInstance.getValue();

      Object newAM = Proxy.newProxyInstance(
          Thread.currentThread().getContextClassLoader(),
          new Class<?>[]{Reflect.with("android.app.IActivityManager").getClazz()},
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
          PLLogger.d(TAG, "LAUNCH_ACTIVITY: " + msg.obj);

          try {
            Intent intent = Reflect.with(msg.obj).injector()
                .field("intent")
                .get();

            Intent raw = intent.getParcelableExtra("rawIntent");
            if (raw != null) {
              intent.setComponent(raw.getComponent());
            }
          } catch (Exception e) {
            PLLogger.w(TAG, e);
          }

          break;
      }

      mH.handleMessage(msg);
      return true;
    }
  }

  private static void hookActivityThreadH() {
    try {
      ActivityThread.mirror(ActivityThread.class);
      Object sCurrentActivityThread = ActivityThread.sCurrentActivityThread.getValue();

      ActivityThread activityThread = MirrorClass.mirror(sCurrentActivityThread,
          ActivityThread.class);

      Handler mH = activityThread.mH.getValue();

      Reflect.with(mH).injector()
          .field("mCallback")
          .set(new HCallbackProxy(mH));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void hook() {
    hookActivityManager();
    hookActivityThreadH();
  }
}
