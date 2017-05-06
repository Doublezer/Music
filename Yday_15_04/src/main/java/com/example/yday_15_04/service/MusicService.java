package com.example.yday_15_04.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

import java.io.IOException;

import static android.R.attr.data;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener,MediaPlayer.OnCompletionListener{

    private MediaPlayer mediaPlayer;
    private String datares;

    public MusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
    private void onCreatMediaPlayer(){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }
    private void processPlay(){
        if (mediaPlayer==null){
            onCreatMediaPlayer();
            try {
                mediaPlayer.setDataSource(datares);
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(datares);
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String str=intent.getStringExtra("actionKey");
        switch (str){
            case "pause":
                mediaPlayer.pause();
                break;
            case "play":
                mediaPlayer.start();
                break;
            default:
                datares=str;
                processPlay();;
                break;
        }


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }
}
