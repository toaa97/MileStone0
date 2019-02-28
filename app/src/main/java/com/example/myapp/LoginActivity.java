package com.example.myapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    //initializing variables
    private Button Login;
    private Button Register;
    private Button googleSignIn;
    private EditText Email;
    private EditText Password;
    //provides backend services, easy-to-use SDKs, and ready-made UI libraries to authenticate users to your app
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    //RC_SIGN in is the request code you will assign for starting the new activity. this can be any number.
    // When the user is done with the subsequent activity and returns, the system calls your activity's onActivityResult() method
    private static final int  RC_SIGN_IN=1;
    //google user
    public GoogleApiClient mGoogleSignInClient;
    //They show up in the "tag" column in LogCat, where it can be used for searching and filtering messages.
    // The function is just passing them through.
    private static final String TAG="LoginActivity";


    @Override
    //onCreate(Bundle) is where you initialize your activity.
    protected void onCreate(Bundle savedInstanceState) {
        //The savedInstanceState is a reference to a Bundle object that is passed into the onCreate method of every Android Activity.
        // Activities have the ability, under special circumstances, to restore themselves to a previous state using the data stored in this bundle
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //creating variables and getting current user
        //obtain an instance of this class
        firebaseAuth=FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()!=null){
            //finish();
            startActivity(new Intent(LoginActivity.this,ListActivity.class));
        }

        //ProgressDialog is a modal dialog, which prevents the user from interacting with the app.
        progressDialog=new ProgressDialog(this);
        Login=(Button) findViewById(R.id.edtLogin); //casting with button?
        Register=(Button)findViewById(R.id.edtRegister);
        Email=(EditText)findViewById(R.id.edtEmail);
        Password=(EditText)findViewById(R.id.edtPassword);
        googleSignIn=(Button)findViewById(R.id.edtGoogle);

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

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("874551466482-tjvm46r9lsvnj1k5k4qefs8ocmj5po1r.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient= new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginActivity.this,"Connection To Google Failed",Toast.LENGTH_LONG).show();


                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            progressDialog.setMessage("Logging In With Google Account,Please Wait...");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();

            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if(result.isSuccess()){
                GoogleSignInAccount account=result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                Toast.makeText(LoginActivity.this,"Getting Authentication Results...",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(LoginActivity.this,"Couldn't Get Authentication Results",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
            }
        }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            Intent intent=new Intent(LoginActivity.this,ListActivity.class);
                            startActivity(intent);
                            finish();
                            progressDialog.dismiss();

                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            String message=task.getException().toString();
                            Intent intent=new Intent(LoginActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(LoginActivity.this,"Not Authenticated: " + message,Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }


    //login method
    private void login(){
        //eliminates leading and trailing spaces
        String email=Email.getText().toString().trim();
        String password=Password.getText().toString().trim();


        //it is simply a set of utility functions to do operations on String objects
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please Enter Email",Toast.LENGTH_LONG).show();
            return;
        }


        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please Enter Password",Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Logging In,Please Wait...");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    finish();
                    Intent intent=new Intent(LoginActivity.this,ListActivity.class);
                    startActivity(intent);

                }
                else{
                    Toast.makeText(LoginActivity.this,"Could Not Log In...Please Try Again",Toast.LENGTH_LONG).show();
                }
            }
        });

        }
    }




