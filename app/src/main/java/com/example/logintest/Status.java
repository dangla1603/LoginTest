package com.example.logintest;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.net.Socket;

public class Status extends AppCompatActivity {
    private Socket s;
    TextView txt;
    Button btn;
    Activity activity_ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        txt = findViewById(R.id.editText);
        btn = findViewById(R.id.button);
        //um = findViewById(R.id.editText4);

        getSupportActionBar().setTitle("Back");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        activity_ctx = this;

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String QUERY_TEMPERATURE_DATA = "2";                    // send signal " command 2 " to pi

                new TCPClient("192.168.1.4", 8000, activity_ctx).execute(QUERY_TEMPERATURE_DATA);

            }
        });


    }
}
