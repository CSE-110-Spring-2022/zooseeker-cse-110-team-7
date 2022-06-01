package com.example.zooseekercse110team7;


import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.zooseekercse110team7.map_v2.AssetLoader;
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
        AssetLoader assetLoader = AssetLoader
                .getInstance()
                .loadAssets(
                        "sample_zoo_graph.json",
                        "sample_node_info.json",
                        "sample_edge_info.json",
                        null
                );

        //MapGraph.getInstance().setAssetLoader(assetLoader);
        Path path = Path.getInstance();

        String[] tags = {"alligator", "reptile", "gator"};
        NodeItem n = new NodeItem("flamingo", null,"Flamingos", "exhibit", Arrays.asList(tags),0,0);
        NodeItem n2 = new NodeItem("capuchin", null,"Capuchin Monkeys", "exhibit", Arrays.asList(tags),0,0);
        NodeItem n3 = new NodeItem("koi", null,"Koi Fish", "exhibit", Arrays.asList(tags), 0, 0);
        List<NodeItem> nodeList = new ArrayList<>(); nodeList.add(n); nodeList.add(n2); nodeList.add(n3);
        List<RouteItem> routeItems = path.getShortestPath("entrance_exit_gate", nodeList,"entrance_exit_gate");
        routeItems = path.getShortestPath(nodeList);
    }

    @Test
    public void TestPath(){
        AssetLoader assetLoader = AssetLoader
                .getInstance()
                .loadAssets(
                        "sample_zoo_graph.json",
                        "sample_node_info.json",
                        "sample_edge_info.json",
                        null
                );

        //MapGraph.getInstance().setAssetLoader(assetLoader);
        Path path = Path.getInstance();

        String[] tags = {"alligator", "reptile", "gator"};
        NodeItem n = new NodeItem("koi", null,"Koi Fish", "exhibit", Arrays.asList(tags), 0, 0);
        NodeItem n2 = new NodeItem("flamingo", null,"Flamingos", "exhibit", Arrays.asList(tags),0,0);
        NodeItem n3 = new NodeItem("capuchin", null,"Capuchin Monkeys", "exhibit", Arrays.asList(tags),0,0);
        List<NodeItem> nodeList = new ArrayList<>(); nodeList.add(n); nodeList.add(n2); nodeList.add(n3);
        List<RouteItem> routeItems = path.getShortestPath("entrance_exit_gate", nodeList,"entrance_exit_gate");
        routeItems = path.getShortestPath(nodeList);
    }
}
