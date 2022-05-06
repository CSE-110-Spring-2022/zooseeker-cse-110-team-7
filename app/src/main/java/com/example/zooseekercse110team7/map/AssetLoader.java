package com.example.zooseekercse110team7.map;

//import org.jgrapht.Graph;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import java.util.Map;

import org.jgrapht.Graph;

public class AssetLoader {

    private String zooGraphJSON;
    private String nodeInfoJSON;
    private String edgeInfoJSON;
    Graph<String, IdentifiedWeightedEdge> graph;
    Map<String, ZooData.VertexInfo> vInfo;
    Map<String, ZooData.EdgeInfo> eInfo;

    public AssetLoader(String zooGraph, String nodeInfo, String edgeInfo){
        this.zooGraphJSON = zooGraph;
        this.nodeInfoJSON = nodeInfo;
        this.edgeInfoJSON = edgeInfo;

        graph = ZooData.loadZooGraphJSON(getApplicationContext(), zooGraphJSON);
        vInfo = ZooData.loadVertexInfoJSON(nodeInfoJSON);
        eInfo = ZooData.loadEdgeInfoJSON(edgeInfoJSON);
    }

}
