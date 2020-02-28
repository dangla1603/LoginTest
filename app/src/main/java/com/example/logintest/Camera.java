package com.example.logintest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.pm.PackageManager;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

import android.widget.Toast;
import android.widget.VideoView;
import android.widget.MediaController;


public class Camera extends AppCompatActivity {

    ListView listView;
    ArrayList<String> videoList;
    ArrayAdapter<String> adapter;
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

        // request permission
        verifyStoragePermissions(activity_ctx);

        setContentView(R.layout.activity_camera);
        videoView = findViewById(R.id.videoview);
        listView =  findViewById(R.id.lvideo);


        // Back button
        getSupportActionBar().setTitle("Back");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        videoView.setMediaController(new MediaController(Camera.this));             //add MediaController for the video
        MediaController mediaController = new MediaController(activity_ctx);
        mediaController.setAnchorView(videoView);


        // Query available files on the server.
        String QUERY_LIST = "7";
        new server_connection().execute(QUERY_LIST);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selected_file = listView.getItemAtPosition(position).toString();

                Toast.makeText(getApplicationContext(),"Preparing "+ selected_file,Toast.LENGTH_SHORT).show();

                String VIDEO_GET_REQUEST = "16";
                new server_connection().execute(VIDEO_GET_REQUEST,selected_file,activity_ctx);

            }
        });



       /*
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.example;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView); */
    }

    // Async network procedure for Camera.java
    private class server_connection extends AsyncTask<Object, Void, Object> {

        ProgressDialog progress = new ProgressDialog(activity_ctx);

        String command = "-1";

        @Override
        protected Object doInBackground(Object... voids){
            TCPClient pi = new TCPClient("192.168.1.3", 8000, activity_ctx);
            command = (String) voids[0];

            // Query list of available files. Received data is string value
            if ( command.equals("7") ) {
                return pi.send_command(command);
            }

            // Send GET request of the selected videofile. Return the storage path to the downloaded file.
            if (command.equals("16") ){
                String filename = (String)voids[1];
                String REQUEST = "GET /videos/"+filename;
                return pi.send_command_video(REQUEST, filename,activity_ctx);
            }

            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            // Display loading annimation
        }

        @Override
        protected void onPostExecute(Object result) {
            // Put data on list view
            if ( command.equals("7") ) {

                videoList = new ArrayList<>( Arrays.asList( ((String) result).split(" ") ) );
                adapter = new ArrayAdapter<>(activity_ctx, android.R.layout.simple_list_item_1, videoList);
                listView.setAdapter(adapter);

            }

            // Prepare URI for videoview.
            if (command.equals("16") ){
                String filepath = (String) result;
                videoView.setVideoPath(filepath);
                videoView.requestFocus();
            }
        }

    }
}
