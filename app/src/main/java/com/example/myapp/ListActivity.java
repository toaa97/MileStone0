package com.example.myapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    ListView List;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        List=(ListView)findViewById(R.id.edtListView);
        ArrayList<String> Scroll= new ArrayList<>();
        Scroll.add("Android");
        Scroll.add("iPhone");
        Scroll.add("Windows");
        Scroll.add("Blackberry");
        Scroll.add("Linux");

        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,Scroll);
        List.setAdapter(arrayAdapter);

        List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(ListActivity.this,DeviceDetailActivity.class);
                startActivity(intent);
            }
        });

    }
}
