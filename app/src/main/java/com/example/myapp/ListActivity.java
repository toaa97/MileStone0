package com.example.myapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

public class ListActivity extends AppCompatActivity {

    //initializing variables
    int[] images={R.drawable.android,R.drawable.apple,R.drawable.windows,R.drawable.blackberry,R.drawable.linux};
    String[] names={"Android","iPhone","Windows","Blackberry","Linux"};
    private Button Logout;
    private FirebaseAuth firebaseAuth;
    private GoogleApiClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //creating variables
        firebaseAuth=FirebaseAuth.getInstance();
        Logout=(Button)findViewById(R.id.edtLogoutL);
        ListView CustomList=(ListView)findViewById(R.id.edtCustomList);

        //creating and setting custom adaptor
        CustomAdapter customAdapter=new CustomAdapter();
        CustomList.setAdapter(customAdapter);
        CustomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent(getApplicationContext(),DeviceDetailActivity.class);
                intent.putExtra("name",names[position]);
                intent.putExtra("image",images[position]);
                startActivity(intent);
            }
        });

        //logout process
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.GoogleSignInApi.signOut(mGoogleSignInClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                // ...
                                Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                                firebaseAuth.signOut();
                                Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                                startActivity(i);
                            }
                        });
            }
        });
    }

    //Preventing back pressing
    @Override
    public void onBackPressed(){
        Toast.makeText(ListActivity.this,"Can't Go Back. Log Out",Toast.LENGTH_LONG).show();
    }

    //Custom adapter class
    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView=getLayoutInflater().inflate(R.layout.custom_layout,null);

            ImageView imageView=(ImageView)convertView.findViewById(R.id.edtImage);
            TextView textView=(TextView)convertView.findViewById(R.id.edtText);

            imageView.setImageResource(images[position]);
            textView.setText(names[position]);


            return convertView;
        }
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
