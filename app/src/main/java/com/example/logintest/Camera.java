package com.example.logintest;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;

import java.util.ArrayList;

public class Camera extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        VideoView videoView = findViewById(R.id.videoview);


        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.example;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
    }
}
