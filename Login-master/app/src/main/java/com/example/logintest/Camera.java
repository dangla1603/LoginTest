package com.example.logintest;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class Camera extends AppCompatActivity {

    Button clk;
    VideoView videov;
    MediaController mediaC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clk =(Button) findViewById(R.id.button);
        videov = (VideoView) findViewById(R.id.videoView);
        mediaC = new MediaController(this);


    }

    public void videoplay (View v){
        String videopath = "android.resource://com.example.justcamera/" + R.raw.example;
        Uri uri = Uri.parse(videopath);
        videov.setVideoURI(uri);
        videov.setMediaController(mediaC);
        mediaC.setAnchorView(videov);
        videov.start();

    }
}
