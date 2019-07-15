package io.l0neman.plugintarget;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatButton;

import io.l0neman.plugintarget.activity.AActivity;
import io.l0neman.plugintarget.base.BaseUtilsActivity;
import io.l0neman.plugintarget.service.TargetService;
import io.l0neman.plugintarget.util.TALogger;

@SuppressWarnings("ALL")
public class MainActivity extends BaseUtilsActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    TALogger.d(TAG, "onCreate");

    setContentView(getContentView());
  }

  private View getContentView() {
    LinearLayout contentView = new LinearLayout(this);
    contentView.setOrientation(LinearLayout.VERTICAL);

    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    );

    Button btn1 = new AppCompatButton(this);
    btn1.setLayoutParams(lp);
    btn1.setText("start AActvity");
    click(btn1, new View.OnClickListener() {
      @Override public void onClick(View v) {
        goA();
      }
    });

    Button btn2 = new AppCompatButton(this);
    btn2.setLayoutParams(lp);
    btn2.setText("start TargetService");
    click(btn2, new View.OnClickListener() {
      @Override public void onClick(View v) {
        startTargetService();
      }
    });

    Button btn3 = new AppCompatButton(this);
    btn3.setLayoutParams(lp);
    btn3.setText("access TargetProvider");
    click(btn3, new View.OnClickListener() {
      @Override public void onClick(View v) {
        accessTargetProvider();
      }
    });

    Button btn4 = new AppCompatButton(this);
    btn4.setLayoutParams(lp);
    btn4.setText("bind TargetService");
    click(btn4, new View.OnClickListener() {
      @Override public void onClick(View v) {
        bindTargetService();
      }
    });

    Button btn5 = new AppCompatButton(this);
    btn5.setLayoutParams(lp);
    btn5.setText("stop TargetService");
    click(btn5, new View.OnClickListener() {
      @Override public void onClick(View v) {
        stopTargetService();
      }
    });

    contentView.addView(btn1);
    contentView.addView(btn2);
    contentView.addView(btn3);
    contentView.addView(btn4);
    contentView.addView(btn5);

    return contentView;
  }

  private void goA() {
    startActivity(new Intent(this, AActivity.class));
  }

  private void startTargetService() {
    startService(new Intent(this, TargetService.class));
  }

  private void accessTargetProvider() {
    final Bundle call = getContentResolver().call(Uri.parse("content://io.l0neman.target.provider/"),
        "access", "param", new Bundle());
    TALogger.d(TAG, "access TargetProvider: " + call);
  }

  private ServiceConnection mConn;

  private void bindTargetService() {
    if (mConn != null) {
      return;
    }

    mConn = new ServiceConnection() {
      @Override public void onServiceConnected(ComponentName name, IBinder service) {
        TALogger.d(TAG, "bind TargetService: " + service);
      }

      @Override public void onServiceDisconnected(ComponentName name) {
        TALogger.d(TAG, "unbind TargetService: " + name);
      }
    };
    bindService(new Intent(this, TargetService.class),
        mConn, Context.BIND_AUTO_CREATE);
  }

  private void stopTargetService() {
    stopService(new Intent(this, TargetService.class));
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    TALogger.d(TAG, "onSaveInstanceState");
  }

  @Override protected void onStart() {
    super.onStart();
    TALogger.d(TAG, "onStart");
  }

  @Override protected void onRestart() {
    super.onRestart();
    TALogger.d(TAG, "onRestart");
  }

  @Override protected void onResume() {
    super.onResume();
    TALogger.d(TAG, "onResume");
  }

  @Override protected void onPause() {
    super.onPause();
    TALogger.d(TAG, "onPause");
  }

  @Override protected void onStop() {
    super.onStop();
    TALogger.d(TAG, "onStop");
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    TALogger.d(TAG, "onDestroy");

    if (mConn != null) {
      unbindService(mConn);
    }
  }
}
