package com.example.zooseekercse110team7;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.zooseekercse110team7.map.AssetLoader;
import com.example.zooseekercse110team7.map.CalculateShortestPath;
import com.example.zooseekercse110team7.map_v2.Graph;
import com.example.zooseekercse110team7.map_v2.Path;
import com.example.zooseekercse110team7.planner.NodeDao;
import com.example.zooseekercse110team7.planner.NodeDatabase;
import com.example.zooseekercse110team7.planner.NodeItem;

import org.junit.*;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ShortestPathTest {

    @Test
    public void testShortPathReturn(){
        String start = "entrance_exit_gate";
        String goal = "elephant_odyssey";
        AssetLoader assets = new AssetLoader(
                "sample_zoo_graph.json",
                "sample_node_info.json",
                "sample_edge_info.json",
                null);
        CalculateShortestPath tester = new CalculateShortestPath(start, goal, assets);

        System.out.println(tester.getShortestPath());

        String start2 = "lions";
        String goal2 = "gorillas";
        AssetLoader assets2 = new AssetLoader(
                "sample_zoo_graph.json",
                "sample_node_info.json",
                "sample_edge_info.json",
                null);
        CalculateShortestPath tester2 = new CalculateShortestPath(start2, goal2, assets2);

        System.out.println(tester2.getShortestPath());
    }

    private NodeDao nodeDao;
    private NodeDatabase db;

    @Before
    public void CreateDatabase(){
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, NodeDatabase.class)
                .allowMainThreadQueries()
                .build();
        nodeDao = db.nodeDao();
    }

    @After
    public void CloseDatabase(){
        db.close();
    }
    @Test
    public void TestPath(){
        AssetLoader assetLoader = new AssetLoader(
                "sample_zoo_graph.json",
                "sample_node_info.json",
                "sample_edge_info.json",
                null);
        Graph graph = new Graph(assetLoader);
        Path path = new Path(graph);
        String[] tags = {"alligator", "reptile", "gator"};
        NodeItem n = new NodeItem("gators", "Alligators", "exhibit", Arrays.asList(tags));
        List<NodeItem> nodeList = new ArrayList<>(); nodeList.add(n);
        path.getShortestPath("entrance_exit_gate", nodeList,"lions");
    }
}
