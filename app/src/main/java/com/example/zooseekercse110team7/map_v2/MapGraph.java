package com.example.zooseekercse110team7.map_v2;

import android.util.Pair;

import com.example.zooseekercse110team7.GlobalDebug;
import com.example.zooseekercse110team7.map.AssetLoader;
import com.example.zooseekercse110team7.map.EdgeInfo;
import com.example.zooseekercse110team7.map.IdentifiedWeightedEdge;
import com.example.zooseekercse110team7.map.VertexInfo;

import org.jgrapht.Graph;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * IMPORTANT NOTE: name = id of Node Items
 * */
public class MapGraph {
    public static MapGraph instance = new MapGraph();

    AssetLoader assetLoader;
    private Map<String, Integer> idMap;

    private MapGraph(){}
    public static MapGraph getInstance(){ return instance; }
    public void setAssetLoader(AssetLoader assetLoader){
        this.assetLoader = assetLoader;
        idMap = new HashMap<>();
        createIdMap();
    }

    public Graph<String, IdentifiedWeightedEdge> getGraph(){
        return assetLoader.getGraph();
    }

    public double getEdgeWeight(String from, String to){
        IdentifiedWeightedEdge edge = assetLoader.getGraph().getEdge(from, to);
        return assetLoader.getGraph().getEdgeWeight(edge);
    }

    /**
     * Gets edges/neighbors of a vertex
     * */
    public Set<IdentifiedWeightedEdge> getEdges(String vertex){
        return assetLoader.getGraph().edgesOf(vertex);
    }

    public Map<String, Double> getNeighborsMap(String vertex){
        Map<String, Double> result = new HashMap<String, Double>();
        Set<IdentifiedWeightedEdge> edges = getEdges(vertex);
        String target;
        for(IdentifiedWeightedEdge edge: edges){
            target = edge.getEdgeTarget();
            if(target.equals(vertex)){
                result.put(edge.getEdgeSource(), getEdgeWeight(vertex, edge.getEdgeSource()));
                continue;
            }//this may cause algorithim to not work
            result.put(edge.getEdgeTarget(), getEdgeWeight(vertex, edge.getEdgeTarget()));
        }

        return result;
    }

    public Integer getId(String vertex){
        return idMap.get(vertex);
    }

    /**
     * ONLY NEEDED FOR DEBUGGING -- SHOULD NOT BE CALLED TO RETRIEVE
     * */
    public Map<String, Integer> getIdMap(){ return (GlobalDebug.DEBUG)?idMap:null; }

    public int getMatrixSize(){ return idMap.size(); }

    /**
     * Returns name of the neighbor
     * */
    public String getNeighborName(IdentifiedWeightedEdge neighbor, String aVertex){
        if(neighbor.getEdgeSource().equals(aVertex)){ return neighbor.getEdgeTarget(); }
        return neighbor.getEdgeSource();
    }

    /**
     * Creates a HashMap for each vertex in the graph which will be used in to find the shortest
     * path.
     * */
    private void createIdMap(){
        int idNumber = 0;
        Map<String, VertexInfo> vertexMap = assetLoader.getVertexMap();
        for(Map.Entry<String, VertexInfo> aVertex: vertexMap.entrySet()){
            idMap.put(aVertex.getValue().id, idNumber);
            idNumber++;
        }
    }
}
