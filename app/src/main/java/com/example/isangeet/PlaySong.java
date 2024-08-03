package com.example.isangeet;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class PlaySong extends AppCompatActivity {
    TextView textView;
    ImageView playPause, previousTrack, nextTrack, alarm;
    ArrayList<File> songs;
    MediaPlayer mediaPlayer;
    String currentSong;
    int position;
    Thread updateSeekbar;
    SeekBar seekBar;
    TextView newAlarm;
    ImageView logo;

    TimePicker alarmTimePicker;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_play_song);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textView = findViewById(R.id.songName);
        playPause = findViewById(R.id.playPause);
        previousTrack = findViewById(R.id.previousTrack);
        nextTrack = findViewById(R.id.nextTrck);
        seekBar = findViewById(R.id.seekbar);
        alarm = findViewById(R.id.alarm);
        newAlarm = findViewById(R.id.newAlarm);
        logo = findViewById(R.id.musicLogo);

        textView.setSelected(true);

        alarmTimePicker = (TimePicker) findViewById(R.id.timePicker);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("songlist");
        currentSong = intent.getStringExtra("currentSong");
        textView.setText(currentSong);

        position = intent.getIntExtra("position", 0);

        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        updateSeekbar = new Thread() {
            @Override
            public void run() {
                super.run();
                int currentPosition = 0;
                try {
                    while (currentPosition < mediaPlayer.getDuration()) {
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                        sleep(800);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        updateSeekbar.start();

        playPause.setOnClickListener(view -> {
            if (mediaPlayer.isPlaying()) {
                playPause.setImageResource(R.drawable.ic_play);
                mediaPlayer.pause();
            } else {
                playPause.setImageResource(R.drawable.ic_pause);
                mediaPlayer.start();
            }
        });

        previousTrack.setOnClickListener(view -> {
            mediaPlayer.stop();
            mediaPlayer.release();

            if (position != 0) {
                position = position - 1;
            } else {
                position = songs.size() - 1;
            }

            Uri uri1 = Uri.parse(songs.get(position).toString());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri1);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration());

            currentSong = songs.get(position).getName();
            textView.setText(currentSong);

            playPause.setImageResource(R.drawable.ic_pause);

        });


        logo.setOnClickListener(view -> {
                Intent in = new Intent(PlaySong.this, NotificationActivity.class);
                startActivity(in);

        });

        nextTrack.setOnClickListener(view -> {
            mediaPlayer.stop();
            mediaPlayer.release();

            if (position != songs.size() - 1) {
                position = position + 1;
            } else {
                position = 0;
            }

            Uri uri12 = Uri.parse(songs.get(position).toString());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri12);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration());

            currentSong = songs.get(position).getName();
            textView.setText(currentSong);

            playPause.setImageResource(R.drawable.ic_pause);
        });

        alarm.setOnClickListener(view -> {
            /*Intent intent = new Intent(PlaySong.this, SetAlarm.class);
            startActivity(intent);*/

            // Get an instance of Calendar to get current time
            Calendar c = Calendar.getInstance();

            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            int ap = c.get(Calendar.AM_PM);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    PlaySong.this,
                    (view1, hourOfDay, minute1) -> {
                        // Set the selected time to a TextView or perform any other action
                        newAlarm.setText(hourOfDay + ":" + minute1);

                        setAlarm(c);
                        //Toast.makeText(PlaySong.this, "Alarm Set", Toast.LENGTH_SHORT).show();
                    },
                    hour, // Initial hour of the TimePickerDialog
                    minute,// Initial minute of the TimePickerDialog
                    false // Whether the TimePickerDialog is in 24-hour mode or not
            );

            timePickerDialog.show();

        });

    }

    public void setAlarm(Calendar c){
       /* EditText text = findViewById(R.id.time);
        int i = Integer.parseInt(text.getText().toString());
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 234324243, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + (i * 1000), pendingIntent);
        Toast.makeText(this, "Alarm set in " + i + " seconds",Toast.LENGTH_LONG).show();*/

        long time;

        Toast.makeText(PlaySong.this, "ALARM ON", Toast.LENGTH_SHORT).show();
        /*Calendar calendar = Calendar.getInstance();

        // calendar is called to get current time in hour and minute
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute1);*/

        // BroadcastReceiver
        Intent intent = new Intent(this, MyReceiver.class);

        // we call broadcast using pendingIntent
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        time = (c.getTimeInMillis() - (c.getTimeInMillis() % 60000));
        if (System.currentTimeMillis() > time) {
            // setting time as AM and PM
            if (Calendar.AM_PM == 0)
                time = time + (1000 * 60 * 60 * 12);
            else
                time = time + (1000 * 60 * 60 * 24);
        }
        // Alarm rings continuously until toggle button is turned off
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, 800, pendingIntent);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        //createNotificationChannel();

        //Intent in = new Intent(getApplicationContext(), RunningService.class);

        intent.setAction(RunningService.Actions.START.toString());
        startService(intent);
    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeekbar.interrupt();
    }*/
}