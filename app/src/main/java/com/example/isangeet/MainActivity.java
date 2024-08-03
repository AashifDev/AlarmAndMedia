package com.example.isangeet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.session.MediaStyleNotificationHelper;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.permissionx.guolindev.PermissionX;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("ScheduleExactAlarm")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(new String[]{Manifest.permission.READ_MEDIA_AUDIO
                    , Manifest.permission.POST_NOTIFICATIONS}, 99);
        } else {
            Dexter.withContext(this)
                    .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
//                    .withListener(new PermissionListener() {
//                        @Override
//                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                            listView = findViewById(R.id.listView);
//                            ArrayList<File> mySongs = fetchSong(Environment.getExternalStorageDirectory());
//                            String[] items = new String[mySongs.size()];
//                            for (int i = 0; i < mySongs.size(); i++) {
//                                items[i] = mySongs.get(i).getName().replace(".mp3", "");
//                            }
//                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, items);
//                            listView.setAdapter(arrayAdapter);
//
//                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                                    Intent intent = new Intent(MainActivity.this, PlaySong.class);
//                                    String currentSong = listView.getItemAtPosition(position).toString();
//                                    intent.putExtra("songlist", mySongs);
//                                    intent.putExtra("currentSong", currentSong);
//                                    intent.putExtra("position", position);
//                                    startActivity(intent);
//
//                                }
//                            });
//                        }
//                        @Override
//                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {}
//                        @Override
//                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
//                            permissionToken.continuePermissionRequest();
//                        }
//                    })
//                    .check();

                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                            if (multiplePermissionsReport.areAllPermissionsGranted()) {
                                Toast.makeText(MainActivity.this,"granted",Toast.LENGTH_SHORT).show();
                                listView = findViewById(R.id.listView);
                                ArrayList<File> mySongs = fetchSong(Environment.getExternalStorageDirectory());
                                String[] items = new String[mySongs.size()];
                                for (int i = 0; i < mySongs.size(); i++) {
                                    items[i] = mySongs.get(i).getName().replace(".mp3", "");
                                }
                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, items);
                                listView.setAdapter(arrayAdapter);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                        Intent intent = new Intent(MainActivity.this, PlaySong.class);
                                        String currentSong = listView.getItemAtPosition(position).toString();
                                        intent.putExtra("songlist", mySongs);
                                        intent.putExtra("currentSong", currentSong);
                                        intent.putExtra("position", position);
                                        startActivity(intent);

                                    }
                                });
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    })
                    .onSameThread().check();
        }

        Log.d("sdkkkk", getAndroidVersion().toString());
    }

    public String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return "Android SDK: " + sdkVersion + " (" + release + ")";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 99) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                listView = findViewById(R.id.listView);
                ArrayList<File> mySongs = fetchSong(Environment.getExternalStorageDirectory());
                String[] items = new String[mySongs.size()];
                for (int i = 0; i < mySongs.size(); i++) {
                    items[i] = mySongs.get(i).getName().replace(".mp3", "");
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, items);
                listView.setAdapter(arrayAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Intent intent = new Intent(MainActivity.this, PlaySong.class);
                        String currentSong = listView.getItemAtPosition(position).toString();
                        intent.putExtra("songlist", mySongs);
                        intent.putExtra("currentSong", currentSong);
                        intent.putExtra("position", position);
                        startActivity(intent);

                        Intent in = new Intent(getApplicationContext(), RunningService.class);

                        in.setAction(RunningService.Actions.START.toString());
                        startService(in);


                    }
                });
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();

            }
        }
    }


    public ArrayList<File> fetchSong(File file) {
        ArrayList arrayList = new ArrayList();
        File[] songs = file.listFiles();
        if (songs != null) {
            for (File singleFile : songs) {
                if (singleFile.isDirectory() && !singleFile.isHidden()) {
                    arrayList.addAll(fetchSong(singleFile));
                } else {
                    if (singleFile.getName().endsWith(".mp3") && !singleFile.getName().startsWith(".")) {
                        arrayList.add(singleFile);
                    }
                }
            }
        }
        return arrayList;
    }

}