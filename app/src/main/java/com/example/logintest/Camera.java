package com.example.logintest;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;

public class Camera extends AppCompatActivity {

    ListView listView;
    ArrayList<String> videoList;
    ArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        getSupportActionBar().setTitle("Back");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final VideoView videoView = findViewById(R.id.videoview);
        listView =  findViewById(R.id.lvideo);
        videoList = new ArrayList<>();                  // Make ArrayList for video
        // TODO: tcp send_command -> string -> list of available files -> for loop videoList.add() 
        videoList.add("video");
        videoList.add("video1");
        videoList.add("video2");
        videoList.add("video3");

        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,videoList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(),position+"",Toast.LENGTH_SHORT).show();
                
                // NOTE: if possible try to get the URI path based on the name of the clicked element instead of index 
                switch (position){                                                                              // Make the list of VideoView
 
                    case 0:
                        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName()+ "/" +R.raw.example));
                        break;
                    case 1:
                        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName()+"/" +R.raw.video));
                        break;
                    /* NOTE: 
                    case "video-2020_2_18.mp4":
                            tcp send_command("/GET video-2020_2_18.mp4") 
                            -> file write to sd card with the same name 
                            *wait for file to download*
                            -> videoView.setVideoURI(URI.parse("the path we just write to"))
                    */
                }
                videoView.setMediaController(new MediaController(Camera.this));             //add MediaController for the video
                videoView.requestFocus();
                videoView.start();

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
