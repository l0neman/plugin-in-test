package io.l0neman.plugintarget.base;

import android.util.SparseArray;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by l0neman on 2019/07/13.
 */
public abstract class BaseUtilsActivity extends AppCompatActivity {

  // 缓存。
  private SparseArray<View> mActivityFindViews;

  // id 即表示 view，随时取用。
  protected <T extends View> T $(int id) {
    if (mActivityFindViews == null) {
      mActivityFindViews = new SparseArray<>();
    }

    View view = mActivityFindViews.get(id);
    if (view != null) {
      // noinspection unchecked
      return (T) view;
    }

    view = findViewById(id);
    mActivityFindViews.put(id, view);
    // noinspection unchecked
    return (T) view;
  }

  protected void click(View view, View.OnClickListener clickListener) {
    view.setOnClickListener(clickListener);
  }

  protected void click(int id, View.OnClickListener clickListener) {
    $(id).setOnClickListener(clickListener);
  }
}
