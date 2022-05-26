package com.example.zooseekercse110team7.map_v2;

import android.util.Log;

import com.example.zooseekercse110team7.routesummary.RouteItem;

import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * IMPORTANT NOTE: name = id of Node Items
 *
 * This is a singleton class (so it can be called anywhere) that is related to graph used throughout
 * the project. However, before it can be used anywhere, an `AssetLoader` object needs to be set!
 * Although the `Path` class (which calculates the path) is updates with the graph, this class
 * keeps the most recently calculated path.
 * */
public class MapGraph {
    public static MapGraph instance = new MapGraph();
    private MapGraph(){}
    public static MapGraph getInstance(){ return instance; }

    AssetLoader assetLoader;
    private List<RouteItem> pathOfRouteItems;   // list of items to
    private Integer currentPathIndex = 0;

    /**
     * Sets the asset loader that is used to build the graph
     *
     * @param assetLoader an `AssetLoader` object that has parsed JSON files
     * */
    public void setAssetLoader(AssetLoader assetLoader){
        this.assetLoader = assetLoader;
    }

    /**
     * Returns Java's built in Graph object using our JSON files as the data. Each item in the graph
     * has the name of the edge as with their mapped value to the edge it's associated with
     *
     * @return a Graph object with <String, IdentifiedWeightedEdge>
     * */
    public Graph<String, IdentifiedWeightedEdge> getGraph(){
        return assetLoader.getGraph();
    }


    //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-//


    /**
     * Get the weight of an edge based on two vertices' names
     *
     * @param from source vertex
     * @param to target vertex
     *
     * @return double representing the weight of two vertices
     * */
    public double getEdgeWeight(String from, String to){
        IdentifiedWeightedEdge edge = assetLoader.getGraph().getEdge(from, to);
        return assetLoader.getGraph().getEdgeWeight(edge);
    }

    /**
     * Get the weight of an edge based on a given edge (`IdentifiedWeightedEdge`)
     *
     * @param edge an `IdentifiedWeightedEdge` to get the weight from
     *
     * @return double representing the weight of a given edge
     * */
    public double getEdgeWeight(IdentifiedWeightedEdge edge){
        return assetLoader.getGraph().getEdgeWeight(edge);
    }

    /**
     * Gets edges/neighbors relative to a vertex.
     *
     * @param vertex the vertex to get neighbors from
     *
     * @return Set of `IdentifiedWeightedEdge`s which are neighbors to the vertex
     * */
    public Set<IdentifiedWeightedEdge> getEdges(String vertex){
        return assetLoader.getGraph().edgesOf(vertex);
    }

    /**
     * Gets edges/neighbors relative to a vertex in the form of a map where the key=name and
     * value=weight of each neighbor.
     *
     * @param vertex a vertex of the neighbors to get from
     *
     * @return Map of <Key=String name, Value=Double weight> which are neighbors to the vertex
     * */
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

    /**
     * Returns name of the neighbor based on an edge (`IdentifiedWeightedEdge`) and vertex
     * (neighbor relative to the vertex)
     *
     * @param neighbor an edge which is known to be a neighbor
     * @param aVertex some vertex which is known to be the the neighbor of
     *
     * @return String of the neighbor's name
     * */
    public String getNeighborName(IdentifiedWeightedEdge neighbor, String aVertex){
        if(neighbor.getEdgeSource().equals(aVertex)){ return neighbor.getEdgeTarget(); }
        return neighbor.getEdgeSource();
    }


    //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-//


    /**
     * Updates the graph's most recent path
     *
     * @param newPath a new path to update the current path with
     * */
    public void setPath(List<RouteItem> newPath){ this.pathOfRouteItems = newPath; }

    /**
     * Returns the size of the path `pathOfRouteItems`
     *
     * @return an integer indicating size of path
     * */
    public int getPathSize(){ return pathOfRouteItems.size(); }

    /**
     * This function acts as a singular step within an iteration of a list. Upon call, the current
     * index increases (if possible) and parses a list of `IdentifiedWeightedEdge` (edges) between
     * two exhibits/locations into strings.
     *
     * @return Directions in the form of a String list
     * */
    public List<String> getNextDirections(){//TODO: Make Strings Look Nicer
        List<String> result = new ArrayList<>(); // holds result of parsed string for directions

        /* CHECK FOR EDGE CASES */
        if(pathOfRouteItems == null || currentPathIndex >= getPathSize()){
            Log.d("MapGraph", "[getNextDirections] End of Directions! OR List Is NULL!");
            result.add("End of Directions.");
            return result;
        }

        /* PARSE EDGES TO STRING OF DIRECTIONS */
        RouteItem currentRoute = pathOfRouteItems.get(currentPathIndex);
        List<IdentifiedWeightedEdge> edges = Path
                .getInstance()
                .getPathEdges(currentRoute.getSource(), currentRoute.getDestination());
        for(IdentifiedWeightedEdge edge: edges){
            result.add((edge.toString() + " " + getEdgeWeight(edge) + "\n"));
        }

        /* INCREASE CURRENT INDEX WITHIN PATH LIST */
        currentPathIndex++;

        return result;
    }

    /**
     * This function acts as a singular step within an iteration of a list. Upon call, the current
     * index decreases (if possible) and parses a list of `IdentifiedWeightedEdge` (edges) between
     * two exhibits/locations into strings.
     *
     * @return Directions in the form of a String list
     * */
    public List<String> getPreviousDirections(){ //TODO: Make Strings Look Nicer
        List<String> result = new ArrayList<>();// holds result of parsed string for directions

        /* CHECK FOR EDGE CASES */
        if(pathOfRouteItems == null){
            Log.d("MapGraph", "List Is NULL!");
            result.add("End of Directions.");
            return result;
        }

        /* DECREASE CURRENT INDEX WITHIN PATH LIST */
        currentPathIndex = (currentPathIndex > 0)? currentPathIndex-1: currentPathIndex;

        /* PARSE EDGES TO STRING OF DIRECTIONS */
        RouteItem currentRoute = pathOfRouteItems.get(currentPathIndex);
        List<IdentifiedWeightedEdge> edges = Path
                .getInstance()
                .getPathEdges(currentRoute.getSource(), currentRoute.getDestination());
        for(IdentifiedWeightedEdge edge: edges){
            result.add((edge.toString() + " " + getEdgeWeight(edge) + "\n"));
        }

        return result;
    }
}
