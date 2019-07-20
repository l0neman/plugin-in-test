package io.l0neman.pluginlib.content;

import android.app.Application;

import io.l0neman.pluginlib.util.Singleton;

/**
 * Created by l0neman on 2019/07/20.
 */
public class VContext {
  public static final String PLACEHOLDER = "placeholder";

  private Application mHostContext;

  private static Singleton<VContext> sInstance = new Singleton<VContext>() {
    @Override protected VContext create() {
      return new VContext();
    }
  };

  private VContext() {}

  public void init(Application mHostContext) {
    this.mHostContext = mHostContext;
  }

  public static VContext getInstance() {
    return sInstance.get();
  }

  public <T> T getService(String serviceName) {
    // noinspection unchecked
    return (T) VServiceRegistry.getService(mHostContext, serviceName);
  }
}
