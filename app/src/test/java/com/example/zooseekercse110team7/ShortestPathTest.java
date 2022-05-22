package com.example.zooseekercse110team7;

import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.zooseekercse110team7.map.AssetLoader;
import com.example.zooseekercse110team7.map.CalculateShortestPath;

import org.junit.*;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ShortestPathTest {

    @Test
    public void testShortPathReturn(){
        String start = "entrance_exit_gate";
        String goal = "koi";
        AssetLoader assets = new AssetLoader(
                "sample_zoo_graph.json",
                "sample_node_info.json",
                "sample_edge_info.json",
                null);
        CalculateShortestPath tester = new CalculateShortestPath(start, goal, assets);

        System.out.println(tester.getShortestPath());

        String start2 = "flamingo";
        String goal2 = "koi";
        AssetLoader assets2 = new AssetLoader(
                "sample_zoo_graph.json",
                "sample_node_info.json",
                "sample_edge_info.json",
                null);
        CalculateShortestPath tester2 = new CalculateShortestPath(start2, goal2, assets2);

        System.out.println(tester2.getShortestPath());
    }
}
