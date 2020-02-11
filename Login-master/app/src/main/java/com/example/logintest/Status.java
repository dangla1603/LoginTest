package com.example.logintest;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        txt = findViewById(R.id.editText);
        btn = findViewById(R.id.button);
        //um = findViewById(R.id.editText4);


        // data = pi.send_command(QUERY_TEMPERATURE_DATA);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println(data);

                String QUERY_TEMPERATURE_DATA = "2";                    // send signal " command 2 " to pi

                // String QUERY_EVENTS_DATA = "10";
                //String data;
                TCPClient pi = new TCPClient("192.168.1.2", 8000);

                pi.execute(QUERY_TEMPERATURE_DATA);
                // pi.execute(QUERY_EVENTS_DATA);                      // might make another class to funtction the commands
                while (!pi.isReady()) {

                }

                txt.setText(pi.get_value());

            }
        });


    }
}
