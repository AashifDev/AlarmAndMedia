package com.example.isangeet;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.app.Notification;
import androidx.core.app.NotificationCompat;

public class RunningService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (Actions.START.toString().equals(action)) {
                start();
            } else if (Actions.STOP.toString().equals(action)) {
                stopSelf();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @SuppressLint("ForegroundServiceType")
    private void start() {

        Intent intent = new Intent(getApplicationContext(), MyReceiver.class);
        intent.putExtra("MESSAGE", "Clicked!");


        int flag = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) ? PendingIntent.FLAG_IMMUTABLE : 0;

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(),
                0,
                intent,
                flag
        );


        Notification notification = new NotificationCompat.Builder(this, "channel")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Run is active")
                .setContentText("Elapsed time: 00:50")
                .setOngoing(true)
                .addAction(R.drawable.ic_pause,"Pause",pendingIntent)
                .build();

        startForeground(1, notification);
    }

    public enum Actions {
        START,
        STOP
    }
}


