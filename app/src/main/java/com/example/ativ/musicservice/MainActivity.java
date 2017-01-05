package com.example.ativ.musicservice;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    ProgressBar bar;
    Button play_button;
    boolean isplaying = true;
    BroadcastReceiver r = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra("status", 0);
            bar.setProgress(status);
        }
    };
    IntentFilter filter = new IntentFilter(getIntent().ACTION_MAIN);

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(r, filter);
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(r);
    }

    MyBoundService srv;
    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            MyBoundService.MyBinder binder = (MyBoundService.MyBinder)iBinder;
            srv = binder.getService();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bar = (ProgressBar) findViewById(R.id.progressBar4);
        play_button = (Button) findViewById(R.id.playbutton);
        bar.setVisibility(ProgressBar.VISIBLE);
        bar.setProgress(0);
        bar.setMax(100);
        bind();
    }

    public void bind() {
        Intent intent = new Intent(this, MyBoundService.class);
        bindService(intent, sc, BIND_AUTO_CREATE);
    }
    public void unbind() {
        unbindService(sc);
    }
    public void playMusic(View v) {
        if(srv == null) {
            bind();
        }
        srv.play();
        if(isplaying) {
            play_button.setText("PAUSE");
            isplaying = false;
        } else {
            play_button.setText("PLAY");
            isplaying = true;
        }
    }
    public void stopMusic(View v) {
        srv.stop();
        play_button.setText("PLAY");
        bar.setProgress(0);
        isplaying=true;
    }
}