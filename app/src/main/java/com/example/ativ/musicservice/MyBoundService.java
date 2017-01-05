package com.example.ativ.musicservice;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class MyBoundService extends Service {
    private final IBinder myBinder = new MyBinder();
    MediaPlayer mp;

    Thread myThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                while(!Thread.currentThread().isInterrupted() && mp.isPlaying()) {
                    Thread.sleep(100);
                    Intent i = new Intent();
                    i.setAction("android.intent.action.MAIN");
                    float total = mp.getDuration();
                    float current = mp.getCurrentPosition();
                    int percentage = (int)((current / total) * 100);
                    i.putExtra("status", percentage);
                    sendBroadcast(i);
                    if(current == total) {
                        myThread.interrupt();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });

    public class MyBinder extends Binder {
        MyBoundService getService() {
            return MyBoundService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), "MyBind called", Toast.LENGTH_SHORT).show();
        mp = MediaPlayer.create(this, R.raw.music);
        return myBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Toast.makeText(getApplicationContext(), "Unbind", Toast.LENGTH_SHORT).show();
        return super.onUnbind(intent);
    }

    public void play() {
        if(mp==null) {
            mp = MediaPlayer.create(this, R.raw.music);
            mp.start();
            Toast.makeText(getApplicationContext(), "Music completes", Toast.LENGTH_SHORT);
            myThread.start();
        } else {
            if(!mp.isPlaying()) {
                mp.start();
                myThread.start();
            } else {
                mp.pause();
            }
        }
    }
    public void stop() {
        if(mp != null) {
            mp.stop();
            mp = null;
            myThread.interrupt();
        } else {
            Toast.makeText(getApplicationContext(), "error: music is not playing", Toast.LENGTH_SHORT).show();
        }
    }
}
