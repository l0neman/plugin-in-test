package io.l0neman.plugintarget.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import io.l0neman.plugintarget.R;

public class AActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final Button button = new Button(this);
    button.setText("AActivity");
    setContentView(button);
  }
}
