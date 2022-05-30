package com.example.zooseekercse110team7.map_v2;

//import org.jgrapht.Graph;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.content.Context;
import android.util.Log;

import java.util.Map;

import org.jgrapht.Graph;


/**
 * This class loads all the information from the JSON files to be able to create a graph.
 * */
public class AssetLoader {
    private static AssetLoader instance = new AssetLoader();
    private AssetLoader(){}
    public static AssetLoader getInstance(){ return instance;}


    private String zooGraphJSON;
    private String nodeInfoJSON;
    private String edgeInfoJSON;
    private Graph<String, IdentifiedWeightedEdge> graph;
    private Map<String, VertexInfo> vInfo;
    private Map<String, EdgeInfo> eInfo;

    public AssetLoader loadAssets(String zooGraph, String nodeInfo, String edgeInfo, Context aContext){
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

        return this;
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

    public Map<String, VertexInfo> getVertexMap(){
        return this.vInfo;
    }

    public Map<String, EdgeInfo> getEdgeMap(){
        return this.eInfo;
    }
}
