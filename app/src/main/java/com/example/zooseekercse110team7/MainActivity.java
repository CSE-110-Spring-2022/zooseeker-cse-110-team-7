package com.example.zooseekercse110team7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, TodoListActivity.class);

// Enable to go to Map Activity
//        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}