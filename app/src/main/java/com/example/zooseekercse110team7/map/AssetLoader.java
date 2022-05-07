package com.example.zooseekercse110team7.map;

//import org.jgrapht.Graph;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import com.example.zooseekercse110team7.MainActivity;

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

    public String getZooFile(){
        return this.zooGraphJSON;
    }

    public String getNodeFile(){
        return this.nodeInfoJSON;
    }

    public String getEdgeFile(){
        return this.edgeInfoJSON;
    }

    public Graph<String, IdentifiedWeightedEdge> getGraph(){
        return this.graph;
    }

    public Map<String, ZooData.VertexInfo> getVertexMap(){
        return this.vInfo;
    }

    public Map<String, ZooData.EdgeInfo> getEdgeMap(){
        return this.eInfo;
    }
}
