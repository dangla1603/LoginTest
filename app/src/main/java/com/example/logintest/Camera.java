package com.example.logintest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.util.ArrayList;


public class Camera extends AppCompatActivity {

    ListView listView;
    ArrayList<String> videoList;
    ArrayAdapter adapter;
    VideoView videoView;

    Activity activity_ctx;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity_ctx = this;

        getSupportActionBar().setTitle("Back");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // request permission
        verifyStoragePermissions(activity_ctx);

        setContentView(R.layout.activity_camera);
        videoView = findViewById(R.id.videoview);
        listView =  findViewById(R.id.lvideo);


        String QUERY_LIST = "7";
        new TCPClient("192.168.1.4", 8000, activity_ctx).execute(QUERY_LIST);

        videoView.setMediaController(new MediaController(Camera.this));             //add MediaController for the video
        videoView.requestFocus();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selected_file = listView.getItemAtPosition(position).toString();

                Toast.makeText(getApplicationContext(),"Preparing "+ selected_file,Toast.LENGTH_SHORT).show();

                String QUERY_VIDEO = "6";
                String GET_VIDEO = "16";

                new TCPClient("192.168.1.4", 8000,activity_ctx).execute(GET_VIDEO,selected_file,activity_ctx);

            }
        });



       /* String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.example;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView); */
    }
}
