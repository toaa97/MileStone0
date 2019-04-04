package com.example.myapp;

import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DeviceDetailActivity extends AppCompatActivity {

    //initializing variables
    private Button Logout;
    private FirebaseAuth firebaseAuth;
    private TextView celsius;
    private TextView fahrenheit;
    private GoogleApiClient mGoogleSignInClient;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String currentEsp;
    //ListView tempList;
    //ArrayList<Temperature> temp;
    //ArrayAdapter<Temperature> tempAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);

        Intent intent = getIntent();
        currentEsp=intent.getStringExtra("name");

        //creating variables
        firebaseAuth = FirebaseAuth.getInstance();
        Logout = (Button) findViewById(R.id.edtLogoutD);
        celsius = (TextView) findViewById(R.id.txtC);
        fahrenheit = (TextView) findViewById(R.id.txtF);
        //tempList=(ListView)findViewById(R.id.tempList);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        //temp=new ArrayList<>();
        //tempAdapter=new ArrayAdapter<Temperature>(this,R.layout.temp_info,R.id.txtC,temp);


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

        readFromDatabase();
    }

    //getting user
    @Override
    protected void onStart() {
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

    public void readFromDatabase() {
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("file", "Failed to read value.", error.toException());
            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        for ( DataSnapshot ds : dataSnapshot.getChildren() ) {
            if(currentEsp.equals("MyESPT")) {
                Temperature temperature = new Temperature();
                temperature.setCelsius(ds.child("MyESPT").getValue(Temperature.class).getCelsius());
                temperature.setFahrenheit(ds.child("MyESPT").getValue(Temperature.class).getFahrenheit());
                celsius.setText(temperature.getCelsius());
                fahrenheit.setText(temperature.getFahrenheit());
            }
            else{
                Temperature temperature = new Temperature();
                temperature.setCelsius(ds.child("MyESPR").getValue(Temperature.class).getCelsius());
                temperature.setFahrenheit(ds.child("MyESPR").getValue(Temperature.class).getFahrenheit());
                celsius.setText(temperature.getCelsius());
                fahrenheit.setText(temperature.getFahrenheit());
            }
            //tempList.setAdapter(tempAdapter);
        }
    }
}
