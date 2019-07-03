package io.l0neman.pluginintest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;

import io.l0neman.pluginintest.activity.ATest;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    if(event.getActionMasked() == MotionEvent.ACTION_UP) {
      ATest.jump(this);
    }

    return super.onTouchEvent(event);
  }
}
