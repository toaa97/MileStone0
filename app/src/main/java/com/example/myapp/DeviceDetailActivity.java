package com.example.myapp;

import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DeviceDetailActivity extends AppCompatActivity {

    //initializing variables
    private Button Logout;
    private FirebaseAuth firebaseAuth;
    private TextView name;
    private ImageView image;
    private GoogleApiClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);

        //creating variables
        firebaseAuth = FirebaseAuth.getInstance();
        Logout = (Button) findViewById(R.id.edtLogoutD);
        name = findViewById(R.id.textViewD);
        image = findViewById(R.id.imageViewD);


        //getting image and text
        //software mechanism that allows users to coordinate the functions of different activities to achieve a task.
        Intent intent = getIntent();
        name.setText(intent.getStringExtra("name"));
        image.setImageResource((intent.getIntExtra("image", 0)));

        //logout process
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.GoogleSignInApi.signOut(mGoogleSignInClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                // ...
                                Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                                firebaseAuth.signOut();
                                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(i);
                            }
                        });
            }
        });
    }

    //getting user
    @Override
    protected void onStart(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("874551466482-tjvm46r9lsvnj1k5k4qefs8ocmj5po1r.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleSignInClient.connect();
        super.onStart();
    }



}
