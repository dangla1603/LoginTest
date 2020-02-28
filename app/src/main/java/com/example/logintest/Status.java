package com.example.logintest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.AsyncTask;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Status extends AppCompatActivity {
    TextView txt;
    Button btn;
    Activity activity_ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity_ctx = this;

        setContentView(R.layout.activity_status);
        txt = findViewById(R.id.editText);
        btn = findViewById(R.id.button);

        getSupportActionBar().setTitle("Back");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String QUERY_TEMPERATURE_DATA = "2";                    // send signal " command 2 " to pi
                new server_connection().execute(QUERY_TEMPERATURE_DATA);

            }
        });


    }


    // Async network procedure for Status.java
    private class server_connection extends AsyncTask<Object, Void, Object>{

        ProgressDialog progress = new ProgressDialog(activity_ctx);

        @Override
        protected Object doInBackground(Object... voids){
            TCPClient pi = new TCPClient("192.168.1.3", 8000, activity_ctx);
            return pi.send_command((String)voids[0]);
        }

        protected void onProgressUpdate(Integer... progress) {
            // Display loading annimation
        }

        @Override
        protected void onPostExecute(Object result) {
            txt.setText((String)result);

        }
    }
}
