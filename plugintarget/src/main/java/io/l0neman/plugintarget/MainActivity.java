package io.l0neman.plugintarget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import io.l0neman.plugintarget.activity.AActivity;

public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d("NEW_GO", "ggg.");
    Button view = new Button(this);
    view.setText("l0neman");
    setContentView(view);
  }

  public void goA(View view) {
    startActivity(new Intent(this, AActivity.class));
  }
}
