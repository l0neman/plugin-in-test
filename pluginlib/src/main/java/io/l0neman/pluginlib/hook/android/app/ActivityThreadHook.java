package io.l0neman.pluginlib.hook.android.app;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import io.l0neman.pluginlib.mirror.android.app.ActivityThread;
import io.l0neman.pluginlib.support.PLLogger;
import io.l0neman.pluginlib.util.Reflect;
import io.l0neman.pluginlib.util.reflect.mirror.MirrorClass;

public class ActivityThreadHook {

  private static final String TAG = ActivityThreadHook.class.getSimpleName();

  public static class H {

    private static final String TAG = ActivityThreadHook.TAG + ".H";

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
              Intent intent = Reflect.with(msg.obj).injector().field("intent").get();

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

    public static void hook() {
      try {
        ActivityThread.mirror(ActivityThread.class);
        Object sCurrentActivityThread = ActivityThread.sCurrentActivityThread.get();

        ActivityThread activityThread = MirrorClass.mirror(sCurrentActivityThread,
            ActivityThread.class);

        Handler mH = activityThread.mH.get();

        Reflect.with(mH).injector()
            .field("mCallback")
            .set(new HCallbackProxy(mH));

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

}