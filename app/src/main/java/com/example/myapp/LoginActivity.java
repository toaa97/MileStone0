package com.example.myapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    //initializing variables
    private Button Login;
    private Button Register;
    private EditText Email;
    private EditText Password;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //creating variables and getting current user
        firebaseAuth=FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()!=null){
            //finish();
            startActivity(new Intent(LoginActivity.this,ListActivity.class));
        }

        progressDialog=new ProgressDialog(this);
        Login=(Button) findViewById(R.id.edtLogin); //casting with button?
        Register=(Button)findViewById(R.id.edtRegister);
        Email=(EditText)findViewById(R.id.edtEmail);
        Password=(EditText)findViewById(R.id.edtPassword);

        //Login click
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        //register click
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent clkRegister=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(clkRegister);
            }
        });
    }

    //login method
    private void login(){
        String email=Email.getText().toString().trim();
        String password=Password.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please Enter Email",Toast.LENGTH_LONG).show();
            return;
        }


        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please Enter Password",Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Logging In,Please Wait...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    finish();
                    startActivity(new Intent(LoginActivity.this,ListActivity.class));
                }
                else{
                    Toast.makeText(LoginActivity.this,"Could Not Log In...Please Try Again",Toast.LENGTH_LONG).show();
                }
            }
        });

        }


    }

