package com.example.ativ.musicservice;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {
    MediaPlayer mp;
    @Override
    public void onCreate() {
        super.onCreate();
        mp = MediaPlayer.create(this, R.raw.music);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        if(this.mp.isPlaying() && intent.getStringExtra("isPlaying").matches("true")) {
//            Toast.makeText(this, "Music is already playing", Toast.LENGTH_SHORT).show();
//        } else {
//            this.mp.start();
//        }
        String status = intent.getStringExtra("status");
        if(status.matches("idle")) {
            mp.start();
        } else if(status.matches("playing") && mp.isPlaying()) {
            Toast.makeText(this, "Music is already playing", Toast.LENGTH_SHORT).show();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        this.mp.stop();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
