package com.example.project_final.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.project_final.R;

public class AlarmReserve extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("toi","Den Gio Hoan Thanh Cong Viec");
        MediaPlayer mediaPlayer;
        mediaPlayer= MediaPlayer.create(context, R.raw.baothuc);
        mediaPlayer.start();

    }
}