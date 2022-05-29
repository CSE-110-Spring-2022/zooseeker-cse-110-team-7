package com.example.zooseekercse110team7;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.zooseekercse110team7.map_v2.AssetLoader;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AssetLoaderTest {

    @Test
    public void testLoad(){
        AssetLoader assets = AssetLoader
                .getInstance()
                .loadAssets(
                        "sample_zoo_graph.json",
                        "sample_node_info.json",
                        "sample_edge_info.json",
                        null
                );

        //System.out.println(assets.getZooFile());
        //System.out.println(assets.getNodeFile());
        //System.out.println(assets.getEdgeFile());

        assertEquals("sample_zoo_graph.json", assets.getZooFile());
        assertEquals("sample_node_info.json", assets.getNodeFile());
        assertEquals("sample_edge_info.json", assets.getEdgeFile());

        assertNotNull(assets.getGraph());
        assertNotNull(assets.getEdgeMap());
        assertNotNull(assets.getVertexMap());
    }
}