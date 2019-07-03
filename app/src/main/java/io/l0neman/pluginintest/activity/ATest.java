package io.l0neman.pluginintest.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import io.l0neman.pluginintest.R;

public class ATest extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_atest);
  }

  public static void jump(Context context) {
    context.startActivity(new Intent(context, ATest.class));

  }
}
