package com.example.zooseekercse110team7.depreciated_map;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.content.Context;
import android.util.Log;

import com.example.zooseekercse110team7.GlobalDebug;
import com.example.zooseekercse110team7.map_v2.AssetLoader;
import com.example.zooseekercse110team7.planner.NodeItem;
import com.example.zooseekercse110team7.planner.ReadOnlyNodeDao;

import java.util.ArrayList;
import java.util.List;

public class GraphPathSingleton {

    private static GraphPathSingleton instance = new GraphPathSingleton();
    private List<NodeItem> plannedItems;
    private List<CalculateShortestPath> directions = new ArrayList<>();
    int startCounter;
    int goalCounter;
    AssetLoader g;

    private GraphPathSingleton() {

    }

    public static GraphPathSingleton getInstance() {
        return instance;
    }
    public void setNodeItems(List<NodeItem> items) {
        Log.d("GraphPathSingleton", "Setting Items...");
        plannedItems = items;
        Log.d("GraphPathSingleton", "Items Set");
    }
    public void loadAssets(Context context) {
        g = new AssetLoader(
                "sample_zoo_graph.json",
                "sample_node_info.json",
                "sample_edge_info.json",
                context);
    }
    public void updateGraph(ReadOnlyNodeDao nodeDao) {
        startCounter = 0;
        goalCounter = 1;

        // get items on planner
        //plannedItems.clear();
        directions.clear();

        if(GlobalDebug.DEBUG){
            Log.d("GraphPathSingleton", "Items In Planner...");
            for(NodeItem item: plannedItems){
                Log.d("GraphPathSingleton", item.toString());
            }
            Log.d("GraphPathSingleton", "-- End of Items --");
        }

        //`kind` requirements to get entrance/exit gate
        List<Boolean> onPlannerBools = new ArrayList<>(); onPlannerBools.add(true); onPlannerBools.add(false);
        List<String> kinds = new ArrayList<>(); kinds.add("gate");

        //getting entrance and exit gate
        if (plannedItems.isEmpty()) { return; }
        /**
         * ❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌
         * NODE DAO WAS REQUIRED HERE -- FIX
         * */
        Log.d("GraphPathSingleton", "Setting Default Start & End");
        NodeItem defaultStart = nodeDao.getByKind(onPlannerBools, kinds).get(0);  // get entrance/exit
        NodeItem defaultEnd = defaultStart;        // get exit gate
        Log.d("GraphPathSingleton", "Defaults Set");


        String path = "";   // directions received

        // add entrance at start of planner and exit at end of planner
        plannedItems.add(0,defaultStart);


        if(plannedItems.size() > 1){
            plannedItems = sortPlannerList(plannedItems); // sort planned items
            plannedItems.add(defaultEnd);
        }
        else{
            Log.d("GraphPathSingleton", "Nothing in plan.");

            return;
        }


        //plannedItems.add(defaultStart);               // add default start


        /* Find Optimized Path */
        //get directions by iterating through list of `NodeItems`

        Log.d("Directions", "Counter: " + String.valueOf(goalCounter) +
                "\tList Size: " + String.valueOf(plannedItems.size()));
        while (goalCounter < plannedItems.size()) {
            directions.add(
                    new CalculateShortestPath(
                            plannedItems.get(startCounter).id,
                            plannedItems.get(goalCounter).id,
                            g));
            startCounter += 1;
            goalCounter += 1;
            //path += directions.getShortestPath();
        }


    }
    public List<NodeItem> sortPlannerList(List<NodeItem> input){
        List<NodeItem> ret = new ArrayList<NodeItem>();

        ret.add(input.get(0));
        //ret.add(input.get(1));
        //input.remove(0);
        input.remove(0);


        NodeItem current = ret.get(ret.size() - 1);

        while(input.size() > 0){
            CalculateShortestPath item = new CalculateShortestPath(current.id, input.get(0).id, g);
            int shortestDist = item.getShortestDist();
            int recordPosition = 0;
            for(int i = 1; i < input.size(); i++){
                CalculateShortestPath comparison = new CalculateShortestPath(current.id, input.get(i).id, g);
                int compareWith = comparison.getShortestDist();
                if(compareWith < shortestDist){
                    shortestDist = compareWith;
                    recordPosition = i;
                }
            }
            ret.add(input.get(recordPosition));
            current = ret.get(ret.size() - 1);
            input.remove(recordPosition);
        }
        return ret;
    }
    public List<CalculateShortestPath> getPath() {
        if (directions == null) { return null; }
        return this.directions;
    }
}
