package com.example.logintest;

        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;

        import com.google.firebase.auth.FirebaseAuth;

public class SecondActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button logout;
    private Button Camera;
    private Button Temperature;
    private Button Status;

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


        Camera = (Button) findViewById(R.id.btnCamera);

        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this,Camera.class));
            }
        });

        Status = (Button) findViewById(R.id.btnStatus);

        Status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this,Status.class));
            }
        });
    }
}
