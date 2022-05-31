package com.example.zooseekercse110team7.map_v2;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.content.Context;
import android.util.Log;

import java.util.Map;

import org.jgrapht.Graph;

/**
 * This class loads (NOT parse) the JSON files related to the zoo. The data is stored and
 * centralized here. Because this class is a singleton class it allows allows any class to get
 * information about the graph. The parsing of the JSON files is delegated to the `ZooData`
 * */
public class AssetLoader {
    //  Singleton Setup
    // ---------------------------------------START---------------------------------------------- //
    private static AssetLoader instance = new AssetLoader();
    private AssetLoader(){}
    public static AssetLoader getInstance(){ return instance;}
    // ----------------------------------------END----------------------------------------------- //

    /* Path Or File Name Of Respective JSON File */
    private String zooGraphJSONFile;
    private String nodeInfoJSONFile;
    private String edgeInfoJSONFile;
    // -------------------------------------------------
    private Graph<String, IdentifiedWeightedEdge> graph;// a graph from the resulting JSON files
    private Map<String, VertexInfo> vertexInfoMap;      // HashMap about the vertices in the graph
    private Map<String, EdgeInfo> edgeInfoMap;          // HashMap about the edges in the graph


    /**
     * Loads in the assets from given paths as parameters. We assume that it's a file name because
     * a `Context` is required in which we'll assume all the JSON files are loaded within the
     * `assets` folder. The return value of the function allows for the Chain of Command pattern if
     * needed.
     *
     * @param zooGraphFileName the name of the JSON file regarding the zoo graph
     * @param nodeInfoFileName the name of the JSON file regarding the nodes/vertices of the graph
     * @param edgeInfoFileName the name of the JSON file regarding the edges of the graph
     * @param aContext the application context -- should be `null` when testing
     *
     * @return this instance of the object
     * */
    public AssetLoader loadAssets(String zooGraphFileName,
                                  String nodeInfoFileName,
                                  String edgeInfoFileName,
                                  Context aContext){
        /* ASSIGN/SAVE FILE NAMES */
        this.zooGraphJSONFile = zooGraphFileName;
        this.nodeInfoJSONFile = nodeInfoFileName;
        this.edgeInfoJSONFile = edgeInfoFileName;

        /* *
         * When testing, pass Context as `null` to as the application context changes between
         * emulation and tests. Otherwise, from whichever activity you are on, you need to pass a
         * Context that is `getApplicationContext()`. It Logs a Warning whenever it uses the
         * context for testing.
         * */
        if(aContext == null){
            Log.w("AssetLoader", "WARNING: Using Context For Testing Not Emulation!");
            aContext = getApplicationContext();
        }

        /* GET PARSED DATA */
        graph = ZooDataParser.loadZooGraphJSON(aContext, zooGraphJSONFile);
        vertexInfoMap = ZooDataParser.loadVertexInfoJSON(aContext, nodeInfoJSONFile);
        edgeInfoMap = ZooDataParser.loadEdgeInfoJSON(aContext, edgeInfoJSONFile);

        return this;
    }

    /* THE FOLLOWING FUNCTIONS GET THE FILE NAMES OF THE RESPECTIVE JSON FILES */
    public String getZooFile(){
        return this.zooGraphJSONFile;
    }
    public String getNodeFile(){
        return this.nodeInfoJSONFile;
    }
    public String getEdgeFile(){
        return this.edgeInfoJSONFile;
    }

    /* THE FOLLOWING FUNCTIONS RETURN A DATA STRUCTURE RESPECTIVE TO THE INFORMATION YOU WANT */
    public Graph<String, IdentifiedWeightedEdge> getGraph(){
        return this.graph;
    }
    public Map<String, VertexInfo> getVertexMap(){
        return this.vertexInfoMap;
    }
    public Map<String, EdgeInfo> getEdgeMap(){
        return this.edgeInfoMap;
    }
}
