package com.example.logintest;

        import androidx.appcompat.app.AppCompatActivity;

        import android.app.Activity;
        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.google.firebase.auth.FirebaseAuth;

public class SecondActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button logout;
    private ImageView Camera;
    private ImageView Temperature;
    private ImageView Status;
    private Button Reset;
    Activity activity_ctx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        firebaseAuth = FirebaseAuth.getInstance();

        logout = (Button) findViewById(R.id.btnLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(SecondActivity.this,MainActivity.class));
            }
        });


        Camera = (ImageView) findViewById(R.id.btnCamera);

        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this,Camera.class));
            }
        });

        Status = (ImageView) findViewById(R.id.btnStatus);

        Status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this,Status.class));
            }
        });

        Temperature = (ImageView) findViewById(R.id.btnTemperature);

        Temperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this,Temperature.class));
            }
        });

        Reset = (Button) findViewById(R.id.btnReset);           // this function will reset the device from the app

        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String QUERY_TEMPERATURE_DATA = "reset";                    // send signal " command reset " to pi
                new server_connection().execute(QUERY_TEMPERATURE_DATA);
            }
        });

    }
    // using asyncTask to function reset button
    private class server_connection extends AsyncTask<Object, Void, Object> {

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


        }
    }
}

