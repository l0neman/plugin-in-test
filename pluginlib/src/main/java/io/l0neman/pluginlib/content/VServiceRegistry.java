package io.l0neman.pluginlib.content;

import android.content.Context;

import androidx.collection.ArrayMap;

import java.util.Map;

import io.l0neman.pluginlib.client.placeholder.PlaceholderManager;

public class VServiceRegistry {

  private static final String TAG = VServiceRegistry.class.getSimpleName();

  private static Map<String, ServiceFetcher> sServiceFetcherMap = new ArrayMap<>();

  private VServiceRegistry() {}

  interface ServiceFetcher<T> {
    T getService(Context ctx);
  }

  static abstract class CacheServiceFetcher<T> implements ServiceFetcher<T> {

    private T instance;

    @Override public T getService(Context ctx) {
      if (instance == null) {
        instance = createService(ctx);
      }

      return instance;
    }

    public abstract T createService(Context ctx);
  }

  static {
    registerService(VContext.PLACEHOLDER, new CacheServiceFetcher<PlaceholderManager>() {

      @Override public PlaceholderManager createService(Context ctx) {
        return new PlaceholderManager(ctx);
      }
    });
  }

  private static void registerService(String name, ServiceFetcher serviceFetcher) {
    sServiceFetcherMap.put(name, serviceFetcher);
  }

  public static Object getService(Context context, String name) {
    final ServiceFetcher serviceFetcher = sServiceFetcherMap.get(name);
    return serviceFetcher != null ? serviceFetcher.getService(context) : null;
  }
}
