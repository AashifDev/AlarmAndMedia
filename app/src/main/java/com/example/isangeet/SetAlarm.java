package com.example.isangeet;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.time.ZoneId;
import java.util.Calendar;

public class SetAlarm extends AppCompatActivity {
    MaterialTimePicker materialTimePicker;
    TextView timePicker;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    TextView setAlarm, cancelAlarm;
    Calendar calender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_set_alarm);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        timePicker = (TextView) findViewById(R.id.timePicker);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        // we call broadcast using pendingIntent
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        /*materialTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Alarm Time")
                .build();

        materialTimePicker.show(getSupportFragmentManager(),"timepicker");
        materialTimePicker.addOnPositiveButtonClickListener(view -> {

            if (materialTimePicker.getHour() > 12) {
                timePicker.setText(String.format("%02d:%02d PM", materialTimePicker.getHour() - 12, materialTimePicker.getMinute()));
            }else {
                //timePicker.setText(String.format("%02d:%02d AM", materialTimePicker.getHour(), materialTimePicker.getMinute()));
                timePicker.setText(String.format( materialTimePicker.getHour()+":"+ materialTimePicker.getMinute()+"AM"));
            }

            calender = Calendar.getInstance();
            calender.set(Calendar.HOUR_OF_DAY,materialTimePicker.getHour());
            calender.set(Calendar.MINUTE,materialTimePicker.getMinute());
            calender.set(Calendar.SECOND,0);
            calender.set(Calendar.MILLISECOND,0);


        });*/


        setAlarm = (TextView) findViewById(R.id.set);
        cancelAlarm = (TextView) findViewById(R.id.cancel);

        setAlarm.setOnClickListener(view -> {

            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent in = new Intent(this, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(this, 0, in, PendingIntent.FLAG_IMMUTABLE);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);


            Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
        });

        cancelAlarm.setOnClickListener(view -> {
            //alarmManager.cancel(pendingIntent);
            Intent in = new Intent(this, AlarmReceiver.class);
            // we call broadcast using pendingIntent
            pendingIntent = PendingIntent.getBroadcast(this, 0, in, PendingIntent.FLAG_IMMUTABLE);
            if (alarmManager == null){
                alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            }
            alarmManager.cancel(pendingIntent);

            Toast.makeText(this, "Alarm Cancelled", Toast.LENGTH_SHORT).show();
        });

    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            NotificationChannel channel = new NotificationChannel("channelId","Running Notification", NotificationManager.IMPORTANCE_HIGH);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }


}