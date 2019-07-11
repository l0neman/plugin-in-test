package io.l0neman.pluginlib.hook.android.app;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import io.l0neman.pluginlib.support.PLLogger;
import io.l0neman.pluginlib.util.Reflect;
import io.l0neman.pluginlib.util.reflect.mirror.MirrorClass;

public class ActivityThread {

  private static final String TAG = ActivityThread.class.getSimpleName();

  public static class H {

    private static final String TAG = ActivityThread.TAG + ".H";

    private static final class HCallbackProxy implements Handler.Callback {

      private final Handler mH;

      public HCallbackProxy(Handler mH) {
        this.mH = mH;
      }

      @Override public boolean handleMessage(Message msg) {

        switch (msg.what) {
          case io.l0neman.pluginlib.mirror.android.app.ActivityThread.H.LAUNCH_ACTIVITY:
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

    public static void hook() {
      try {
        io.l0neman.pluginlib.mirror.android.app.ActivityThread.mirror(io.l0neman.pluginlib.mirror.android.app.ActivityThread.class);
        Object sCurrentActivityThread = io.l0neman.pluginlib.mirror.android.app.ActivityThread.sCurrentActivityThread.get();

        io.l0neman.pluginlib.mirror.android.app.ActivityThread activityThread = MirrorClass.mirror(sCurrentActivityThread,
            io.l0neman.pluginlib.mirror.android.app.ActivityThread.class);

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
