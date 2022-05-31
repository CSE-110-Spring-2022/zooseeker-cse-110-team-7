package com.example.zooseekercse110team7;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.example.zooseekercse110team7.map_v2.AssetLoader;
import com.example.zooseekercse110team7.map_v2.MapGraph;
import com.example.zooseekercse110team7.map_v2.Path;
import com.example.zooseekercse110team7.map_v2.PrettyDirections;
import com.example.zooseekercse110team7.map_v2.UserLocation;
import com.example.zooseekercse110team7.planner.NodeDatabase;
import com.example.zooseekercse110team7.planner.ReadOnlyNodeDao;
import com.example.zooseekercse110team7.planner.UpdateNodeDaoRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private final ACTIVITY yourActivity = ACTIVITY.Map;

    enum ACTIVITY{
        Map,
        Planner,
        Search
    }

    ReadOnlyNodeDao nodeDao;
    NodeDatabase db;
    AssetLoader g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        MapGraph.getInstance().setAssetLoader(new AssetLoader(
//                "sample_zoo_graph.json",
//                "sample_node_info.json",
//                "sample_edge_info.json",
//                getApplicationContext()));

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
        UpdateNodeDaoRequest.getInstance().setNodeDao(getApplicationContext());
        Path.getInstance().getShortestPath(nodeDao.getByOnPlanner(true));//on startup get planner info

        // get last open Activity
        String lastActivity = PreferenceManager.getDefaultSharedPreferences(this).getString("last_activity", "");
        if (Objects.equals(lastActivity, MapsActivity.class.getSimpleName())) {
            startActivity(new Intent(this, MapsActivity.class));
        } else if (Objects.equals(lastActivity, PlannerActivity.class.getSimpleName())) {
            startActivity(new Intent(this, PlannerActivity.class));
        }else if (Objects.equals(lastActivity, SearchActivity.class.getSimpleName())) {
            startActivity(new Intent(this, SearchActivity.class));
        } else {
            // assume default activity
            startActivity(new Intent(this, MapsActivity.class));
        }
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
}
