package com.example.zooseekercse110team7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.zooseekercse110team7.map.CurrentMapLoc;

public class MainActivity extends AppCompatActivity {
    private final ACTIVITY yourActivity = ACTIVITY.Map;

    enum ACTIVITY{
        Map,
        Planner,
        Search
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Intent intent = new Intent(this, TodoListActivity.class);

        Intent intent;

        switch (yourActivity){
            case Map:
                intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                break;
            case Planner:
                intent = new Intent(this, PlannerActivity.class);
                startActivity(intent);
                break;
            case Search:
                intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

//// Enable to go to Map Activity
//        Intent intent = new Intent(this, MapsActivity.class);
//// Enable to go to Map Activity / Current Map Location
////        Intent intent = new Intent(this, MapsActivity.class);
////        Intent intent = new Intent(this, CurrentMapLoc.class);
//        startActivity(intent);
    }
}
