package com.example.zooseekercse110team7;


import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnitRunner;

import com.example.zooseekercse110team7.location.Coord;
import com.example.zooseekercse110team7.map_v2.AssetLoader;
import com.example.zooseekercse110team7.map_v2.MapGraph;
import com.example.zooseekercse110team7.map_v2.Path;
import com.example.zooseekercse110team7.map_v2.UserLocation;
import com.example.zooseekercse110team7.map_v2.VertexInfo;
import com.example.zooseekercse110team7.planner.NodeDao;
import com.example.zooseekercse110team7.planner.NodeDatabase;
import com.example.zooseekercse110team7.planner.NodeItem;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class DirectionsTest extends AndroidJUnitRunner {
    private final AssetLoader assetLoader = AssetLoader
            .getInstance()
            .loadAssets(
                    "sample_zoo_graph.json",
                    "sample_node_info.json",
                    "sample_edge_info.json",
                    null
            );
    private NodeDao nodeDao;
    private NodeDatabase db;

    @Before
    public void CreateDatabase(){
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, NodeDatabase.class)
                .allowMainThreadQueries()
                .build();
        nodeDao = db.nodeDao();
        Map<String, VertexInfo> nodesToInsert = assetLoader.getVertexMap();
        for(Map.Entry<String, VertexInfo> aPair: nodesToInsert.entrySet()){
            nodeDao.insert(aPair.getValue().toNodeItem());
        }
    }

    @After
    public void CloseDatabase(){
        db.close();
    }

    @Rule
    public ActivityTestRule<MapsActivity> mYourActivityActivityTestRule =
            new ActivityTestRule<MapsActivity>(MapsActivity.class);
    /**
     * Using one static location and planned route, next directions occur changing target, not
     * the source. Moreover, the directions are detailed.
     * */
    @Test
    public void OnePositionNextTest(){
        ActivityTestRule<MapsActivity> mapsActivityActivityScenario = new ActivityTestRule<MapsActivity>(MapsActivity.class);
        UserLocation.getInstance(mapsActivityActivityScenario.getActivity(), nodeDao, db);
        Coord currentStaticPosition = new Coord(32.73561, -117.1493); // near entrance

        List<NodeItem> plannedItems = new ArrayList<>();
        {
            plannedItems.add(assetLoader.getVertexMap().get("flamingo").toNodeItem());
            plannedItems.add(assetLoader.getVertexMap().get("koi").toNodeItem());
            plannedItems.add(assetLoader.getVertexMap().get("fern_canyon").toNodeItem());
        }

        //update path
        Path.getInstance().getShortestPath(plannedItems);

        List<String> directions;
        // koi
        {
            directions = MapGraph.getInstance().getNextDirections();
            assertEquals(4, directions.size());
            assertEquals(directions.get(0), "entrance_exit_gate -> koi\n");
            assertEquals(directions.get(1), "Proceed on " + "Gate Path" + " for "
                    + 10.0 + "ft towards " + "Front Street / Treetops Way"
                    + "\n");
            assertEquals(directions.get(2), "Proceed on " + "Front Street" + " for "
                    + 30.0 + "ft towards " + "Terrace Lagoon Loop (South)"
                    + "\n");
            assertEquals(directions.get(3), "Proceed on " + "Terrace Lagoon Loop" + " for "
                    + 20.0 + "ft towards " + "Koi Fish"
                    + "\n");
        }

        // koi -> flamingo
        {
            directions = MapGraph.getInstance().getNextDirections();
            assertEquals(directions.size(), 5);
            assertEquals(directions.get(0), "Proceed on " + "Front Street" + " for "
                    + 10 + "ft towards " + "Front Street / Treetops Way"
                    + "\n");
            assertEquals(directions.get(0), "Proceed on " + "Front Street" + " for "
                    + 30 + "ft towards " + "Front Street / Terrace Lagoon Loop (South)"
                    + "\n");
            assertEquals(directions.get(0), "Continue on " + "Front Street" + " for "
                    + 20 + "ft towards " + "Koi"
                    + "\n");
        }

    }
}
