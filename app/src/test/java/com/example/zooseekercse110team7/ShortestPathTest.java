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
    public void testPrint(){
        String start = "entrance_exit_gate";
        String goal = "elephant_odyssey";
        AssetLoader assets = new AssetLoader(
                "sample_zoo_graph.json",
                "sample_node_info.json",
                "sample_edge_info.json",
                null);
        CalculateShortestPath tester = new CalculateShortestPath(start, goal, assets);

        tester.printShortestPath();
    }

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
    }
}
