package com.example.zooseekercse110team7.map;

//import org.jgrapht.Graph;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.content.Context;
import android.util.Log;

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

    public AssetLoader(String zooGraph, String nodeInfo, String edgeInfo, Context aContext){
        this.zooGraphJSON = zooGraph;
        this.nodeInfoJSON = nodeInfo;
        this.edgeInfoJSON = edgeInfo;

        /**
         * When testing, pass Context as `null` to as the application context changes between
         * emulation and tests. Otherwise, from whichever activity you are on, you need to pass a
         * Context that is `getApplicationContext()`. It Logs a Warning whenever it uses the
         * context for testing.
         * */
        if(aContext == null){
            Log.w("AssetLoaderConstructor", "WARNING: Using Context For Testing Not Emulation!");
            aContext = getApplicationContext(); }

        graph = ZooData.loadZooGraphJSON(aContext, zooGraphJSON);
        vInfo = ZooData.loadVertexInfoJSON(aContext, nodeInfoJSON);
        eInfo = ZooData.loadEdgeInfoJSON(aContext, edgeInfoJSON);
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
