package com.example.myapp;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class DeviceDetailActivity extends AppCompatActivity {

    //initializing variables
    private Button Logout;
    private FirebaseAuth firebaseAuth;
    private TextView name;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);

        //creating variables
        firebaseAuth=FirebaseAuth.getInstance();
        Logout=(Button)findViewById(R.id.edtLogoutD);
        name=findViewById(R.id.textViewD);
        image=findViewById(R.id.imageViewD);

        //getting image and text
        Intent intent=getIntent();
        name.setText(intent.getStringExtra("name"));
        image.setImageResource((intent.getIntExtra("image",0)));

        //logout process
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(DeviceDetailActivity.this,LoginActivity.class));
            }
        });
    }
}
