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

public class RegisterActivity extends AppCompatActivity {

    //initializing variables
    private Button Register;
    private Button Login;
    private EditText Email;
    private EditText Password;
    private EditText confirmPass;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //creating variables and getting current user
        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null){
            //finish();
            startActivity(new Intent(RegisterActivity.this,ListActivity.class));
        }
        progressDialog=new ProgressDialog(this);
        Register=(Button)findViewById(R.id.edtRegisterR);
        Login=(Button)findViewById(R.id.edtLoginR);
        Email=(EditText)findViewById(R.id.edtEmailR);
        Password=(EditText)findViewById(R.id.edtPasswordR);
        confirmPass=(EditText)findViewById(R.id.edtConfirmPassR);

        //register click
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registration();
            }
        });

        //login click
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent clkbacktologin=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(clkbacktologin);
            }
        });
    }

    //registration method
    private void registration(){
        String email=Email.getText().toString().trim();
        String password=Password.getText().toString().trim();
        String confirm=confirmPass.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please Enter Email",Toast.LENGTH_LONG).show();
            return;
        }


        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please Enter Password",Toast.LENGTH_LONG).show();
            return;
        }


        if(TextUtils.isEmpty(confirm)) {
            Toast.makeText(this, "Please Confirm Password", Toast.LENGTH_LONG).show();
            return;
        }

        if(!(password.equals(confirm))){
            Toast.makeText(this,"Passwords Don't Match...Re-enter Password",Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Registering,Please Wait...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    finish();
                    Intent openList=new Intent(RegisterActivity.this,ListActivity.class);
                    startActivity(openList);
                }
                else{
                    Toast.makeText(RegisterActivity.this,"Could Not Register...Please Try Again",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
//BaseAdapter as the name suggests, is a base class for all the adapters.