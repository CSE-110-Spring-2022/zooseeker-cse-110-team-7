package com.example.zooseekercse110team7;

import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.zooseekercse110team7.map_v2.AssetLoader;
import com.example.zooseekercse110team7.depreciated_map.CalculateShortestPath;
import com.example.zooseekercse110team7.map_v2.MapGraph;
import com.example.zooseekercse110team7.map_v2.Path;
import com.example.zooseekercse110team7.planner.NodeItem;
import com.example.zooseekercse110team7.routesummary.RouteItem;

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

    @Test
    public void TestPath(){
        AssetLoader assetLoader = new AssetLoader(
                "sample_zoo_graph.json",
                "sample_node_info.json",
                "sample_edge_info.json",
                null);

        MapGraph.getInstance().setAssetLoader(assetLoader);
        Path path = Path.getInstance();

        String[] tags = {"alligator", "reptile", "gator"};
        NodeItem n = new NodeItem("gators", "Alligators", "exhibit", Arrays.asList(tags));
        NodeItem n2 = new NodeItem("elephant_odyssey", "Elephant Odyssey", "exhibit", Arrays.asList(tags));
        NodeItem n3 = new NodeItem("arctic_foxes", "Arctic Foxes", "exhibit", Arrays.asList(tags));
        List<NodeItem> nodeList = new ArrayList<>(); nodeList.add(n); nodeList.add(n2); nodeList.add(n3);
        List<RouteItem> routeItems = path.getShortestPath("entrance_exit_gate", nodeList,"entrance_exit_gate");
        routeItems = path.getShortestPath(nodeList);
    }
}
