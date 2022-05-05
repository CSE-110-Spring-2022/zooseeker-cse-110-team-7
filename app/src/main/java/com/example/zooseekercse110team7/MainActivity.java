package com.example.zooseekercse110team7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //AssetLoader assets = new AssetLoader("sample_zoo_graph.json","sample_node_info.json","sample_edge_info.json");

        //Intent intent = new Intent(this, TodoListActivity.class);

// Enable to go to Map Activity
        Intent intent = new Intent(this, MapsActivity.class);
// Enable to go to Map Activity / Current Map Location
//        Intent intent = new Intent(this, MapsActivity.class);
//        Intent intent = new Intent(this, CurrentMapLoc.class);
        startActivity(intent);
    }
}