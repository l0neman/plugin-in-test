package io.l0neman.pluginlib.hook.android.app;

import android.content.ComponentName;
import android.content.Intent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import io.l0neman.pluginlib.Core;
import io.l0neman.pluginlib.client.placeholder.PlaceholderManager;
import io.l0neman.pluginlib.content.VContext;
import io.l0neman.pluginlib.mirror.android.app.ActivityManagerNative;
import io.l0neman.pluginlib.support.PLLogger;
import io.l0neman.pluginlib.util.Objects;
import io.l0neman.pluginlib.util.Reflect;

public class ActivityManagerNativeHook {

  private static final String TAG = ActivityManagerNativeHook.class.getSimpleName();

  // hook Android L.
  private static class ActivityManagerProxy implements InvocationHandler {

    private final Object mActivityManager;
    private PlaceholderManager mPlaceholderManager;

    ActivityManagerProxy(Object mActivityManager) {
      this.mActivityManager = mActivityManager;

      mPlaceholderManager = VContext.getInstance().getService(VContext.PLACEHOLDER);
    }

    @Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      final String methodName = method.getName();
      PLLogger.d(TAG, "ActivityManager: " + methodName);

      switch (methodName) {
      case "startActivity":
        return handleStartActivity(method, args);
      case "startService":
        return handleStartService(method, args);
      case "stopService":
        return handleStopService(method, args);
      case "bindService":
        return handleBindService(method, args);
      }

      return method.invoke(mActivityManager, args);
    }

    private Object handleStartActivity(Method startActivity, Object[] args) throws Throwable {
      // find raw Intent arg.
      int i = 0;
      while (!(args[i] instanceof Intent)) {
        i++;
      }

      Intent raw = (Intent) args[i];

      // build new Intent.
      Intent newIntent = new Intent();
      newIntent.putExtra("rawIntent", raw);

      String pluginActivityName = Objects.requireNonNull(raw.getComponent()).getClassName();
      String holderActivityClass = mPlaceholderManager.applyActivity(pluginActivityName);

      PLLogger.i(TAG, "[startActivity] proxy: " + pluginActivityName + " => " + holderActivityClass);

      newIntent.setComponent(new ComponentName(
          Core.getInstance().getHostContext().getPackageName(),
          holderActivityClass
      ));

      args[i] = newIntent;
      return startActivity.invoke(mActivityManager, args);
    }

    private Object handleStartService(Method startService, Object[] args) throws Throwable {
      int i = 0;
      while (!(args[i] instanceof Intent)) {
        i++;
      }

      Intent raw = (Intent) args[i];

      String pluginServiceName = Objects.requireNonNull(raw.getComponent()).getClassName();
      String holderServiceClassName = mPlaceholderManager.applyService(pluginServiceName);

      PLLogger.i(TAG, "[startService] proxy: " + pluginServiceName + " => " + holderServiceClassName);

      Intent newIntent = new Intent();
      newIntent.setComponent(new ComponentName(
          Core.getInstance().getHostContext().getPackageName(),
          holderServiceClassName
      ));

      args[i] = newIntent;

      return startService.invoke(mActivityManager, args);
    }

    private Object handleStopService(Method stopService, Object[] args) throws Throwable {
      int i = 0;
      while (!(args[i] instanceof Intent)) {
        i++;
      }

      Intent raw = (Intent) args[i];

      String pluginServiceName = Objects.requireNonNull(raw.getComponent()).getClassName();
      String holderServiceClassName = mPlaceholderManager.applyService(pluginServiceName);

      PLLogger.i(TAG, "[stopService] proxy: " + pluginServiceName + " => " + holderServiceClassName);

      Intent newIntent = new Intent();
      newIntent.setComponent(new ComponentName(
          Core.getInstance().getHostContext().getPackageName(), holderServiceClassName
      ));

      args[i] = newIntent;

      return stopService.invoke(mActivityManager, args);
    }

    private Object handleBindService(Method bindService, Object[] args) throws Throwable {
      int i = 0;
      while (!(args[i] instanceof Intent)) {
        i++;
      }

      Intent raw = (Intent) args[i];

      String pluginServiceName = Objects.requireNonNull(raw.getComponent()).getClassName();
      String holderServiceClassName = mPlaceholderManager.applyService(pluginServiceName);

      PLLogger.i(TAG, "[bindService] proxy: " + pluginServiceName + " => " + holderServiceClassName);

      Intent newIntent = new Intent();
      newIntent.setComponent(new ComponentName(
          Core.getInstance().getHostContext().getPackageName(), holderServiceClassName
      ));

      args[i] = newIntent;

      return bindService.invoke(mActivityManager, args);
    }
  }

  public static void hook() {
    try {
      Object mInstance = ActivityManagerNative.gDefault.mInstance.get();

      Object newAM = Proxy.newProxyInstance(
          Thread.currentThread().getContextClassLoader(),
          new Class<?>[]{Reflect.with("android.app.IActivityManager").getClazz()},
          new ActivityManagerProxy(mInstance)
      );

      ActivityManagerNative.gDefault.mInstance.set(newAM);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
