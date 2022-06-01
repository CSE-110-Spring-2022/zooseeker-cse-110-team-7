package com.example.zooseekercse110team7;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.example.zooseekercse110team7.map_v2.AssetLoader;
import com.example.zooseekercse110team7.map_v2.Path;
import com.example.zooseekercse110team7.map_v2.PrettyDirections;
import com.example.zooseekercse110team7.planner.NodeDatabase;
import com.example.zooseekercse110team7.planner.ReadOnlyNodeDao;
import com.example.zooseekercse110team7.planner.NodeDaoRequest;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ReadOnlyNodeDao nodeDao;
    NodeDatabase db;
    AssetLoader g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* CLASS DEPENDENCIES */
        g = AssetLoader
                .getInstance()
                .loadAssets(
                        "sample_zoo_graph.json",
                        "sample_node_info.json",
                        "sample_edge_info.json",
                        getApplicationContext());                       // parses JSON files

        db = NodeDatabase.getSingleton(getApplicationContext());
        nodeDao = db.nodeDao();
        PrettyDirections.getInstance().setNodeDao(getApplicationContext());
        NodeDaoRequest.getInstance().setNodeDao(getApplicationContext());
        Path.getInstance().getShortestPath(nodeDao.getByOnPlanner(true));//on startup get planner info

        // get last open Activity
        String lastActivity = PreferenceManager.getDefaultSharedPreferences(this).getString("last_activity", "");
        if (Objects.equals(lastActivity, MapsActivity.class.getSimpleName())) {
            startActivity(new Intent(this, MapsActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        } else if (Objects.equals(lastActivity, PlannerActivity.class.getSimpleName())) {
            startActivity(new Intent(this, PlannerActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }else if (Objects.equals(lastActivity, SearchActivity.class.getSimpleName())) {
            startActivity(new Intent(this, SearchActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        } else {
            // assume default activity
            startActivity(new Intent(this, MapsActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
        finish();
        //Intent intent = new Intent(this, TodoListActivity.class);
//
//        Intent intent;
//
//        switch (yourActivity){
//            case Map:
//                intent = new Intent(this, MapsActivity.class);
//                startActivity(intent);
//                break;
//            case Planner:
//                intent = new Intent(this, PlannerActivity.class);
//                startActivity(intent);
//                break;
//            case Search:
//                intent = new Intent(this, SearchActivity.class);
//                startActivity(intent);
//                break;
//            default:
//                break;
//        }

//// Enable to go to Map Activity
//        Intent intent = new Intent(this, MapsActivity.class);
//// Enable to go to Map Activity / Current Map Location
////        Intent intent = new Intent(this, MapsActivity.class);
////        Intent intent = new Intent(this, CurrentMapLoc.class);
//        startActivity(intent);
    }


    // If it comes back to this activity -- Exit Application -- user should not be here
//    @Override
//    protected void onResume(){
//        super.onResume();
//        finish(); // finish intent
//        System.gc(); // garbage collector
//        System.exit(0); // exit application
//    }
}
