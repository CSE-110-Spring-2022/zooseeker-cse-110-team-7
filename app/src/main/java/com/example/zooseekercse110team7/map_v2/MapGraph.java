package com.example.zooseekercse110team7.map_v2;

import android.util.Log;

import com.example.zooseekercse110team7.GlobalDebug;
import com.example.zooseekercse110team7.planner.NodeItem;
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
//    private boolean finishedRouteFlag;

    private MapGraph(){}
    public static MapGraph getInstance(){ return instance; }

    private AssetLoader assetLoader = AssetLoader.getInstance();
    private List<RouteItem> pathOfRouteItems;   // list of items to
    private Integer currentPathIndex = 0;
    private Boolean isGoingBackwards = false;
    private Boolean isBrief = false;

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
        isGoingBackwards = false;
        List<String> result = new ArrayList<>(); // holds result of parsed string for directions

        /* CHECK FOR EDGE CASES */
        if(pathOfRouteItems == null || currentPathIndex >= getPathSize() || 1 == getPathSize()){
            Log.d("MapGraph", "[getNextDirections] End of Directions! OR List Is NULL!");
            result.add("End of Directions.");
            return result;
        }

        /* PARSE EDGES TO STRING OF DIRECTIONS */
        IdentifiedWeightedEdge previousEdge = null;
        RouteItem currentRoute = pathOfRouteItems.get(currentPathIndex);
        result.add((currentRoute.getSource() + " -> " + currentRoute.getDestination() + "\n"));
        List<IdentifiedWeightedEdge> edges = Path
                .getInstance()
                .getPathEdges(currentRoute.getSource(), currentRoute.getDestination());
        for(IdentifiedWeightedEdge edge: edges){
            result.add(StringEdgeParser
                    .getInstance()
                    .toPrettyEdgeString(edge, getEdgeWeight(edge),previousEdge, isBrief)
            );
            previousEdge = edge;
        }

        /* INCREASE CURRENT INDEX WITHIN PATH LIST */
        currentPathIndex++;

        return result;
    }

    /**
     * This function acts as a singular step within an iteration of a list. Upon call, the current
     * index decreases (if possible) and parses a list of `IdentifiedWeightedEdge` (edges) between
     * two exhibits/locations into strings. The directions are in reverse order!
     *
     * @return Directions in the form of a String list
     * */
    public List<String> getPreviousDirections(){ //TODO: Make Strings Look Nicer
        isGoingBackwards = true;
        List<String> result = new ArrayList<>();// holds result of parsed string for directions

        /* CHECK FOR EDGE CASES */
        if(pathOfRouteItems == null  || 1 == getPathSize()){
            Log.d("MapGraph", "List Is NULL!");
            result.add("End of Directions.");
            return result;
        }

        /* DECREASE CURRENT INDEX WITHIN PATH LIST */
        currentPathIndex = (currentPathIndex > 0)? currentPathIndex-1: currentPathIndex;

        /* PARSE EDGES TO STRING OF DIRECTIONS IN REVERSE*/
        IdentifiedWeightedEdge previousEdge = null;
        RouteItem currentRoute = pathOfRouteItems.get(currentPathIndex);
        result.add((currentRoute.getDestination() + " -> " + currentRoute.getSource() + "\n"));
        List<IdentifiedWeightedEdge> edges = Path
                .getInstance()
                .getPathEdges(currentRoute.getDestination(), currentRoute.getSource());
        for(IdentifiedWeightedEdge edge: edges){
            result.add(StringEdgeParser
                    .getInstance()
                    .toPrettyEdgeString(edge, getEdgeWeight(edge),previousEdge, isBrief)
            );
            previousEdge = edge;
        }

        return result;
    }

    //@pre pathOfRouteItems.Size() >= 1
    /**
     * Returns an id string of a node item in the path currently being looked at in the path
     *
     * @return the `id` of the `NodeItem` as a String
     * */
    public String getCurrentItemToVisitId(){
        //if(currentPathIndex - 1 < 0){}
        if(pathOfRouteItems.isEmpty() || 1 == pathOfRouteItems.size()
                || (isGoingBackwards && currentPathIndex+1 >= pathOfRouteItems.size())
                || (!isGoingBackwards && currentPathIndex-1 < 0)){
            return null;
        }
        RouteItem currentRouteItem = pathOfRouteItems.get(((isGoingBackwards)?currentPathIndex+1:currentPathIndex-1));
        Log.d("MapGraph", "Current IDs: [Source] " + currentRouteItem.getSource() + "\t[Destination] "+ currentRouteItem.getDestination());
        Log.d("MapGraph","isGoingBackwards? [true]source : [false]destination -- Bool: " + isGoingBackwards.toString());
        return (isGoingBackwards)?currentRouteItem.getSource():currentRouteItem.getDestination();
    }

    public void updatePathWithRemovedItem(String id){
        //save time by not calculating path
        //there is no need to update current path index as it will point to the next thing in the list
        //      and there are checks/safeguards in place
        if(1 == pathOfRouteItems.size() || pathOfRouteItems.isEmpty()){ pathOfRouteItems = new ArrayList<>(); return; }



        RouteItem previousRouteItem = pathOfRouteItems.get(((isGoingBackwards)?currentPathIndex+1:currentPathIndex-1));
        RouteItem currentRouteItem = pathOfRouteItems.get(currentPathIndex);
        String newSource = (!isGoingBackwards)?previousRouteItem.getSource():currentRouteItem.getSource();
        String newDestination = (!isGoingBackwards)?currentRouteItem.getDestination():previousRouteItem.getDestination();

//        finishedRouteFlag = ((currentRouteItem == pathOfRouteItems.get(0) && isGoingBackwards) ||
//                (currentRouteItem == pathOfRouteItems.get(0)) && !isGoingBackwards);

        List<RouteItem> subpathRouteItems = Path.getInstance().notUpdateGraph().getShortestPath(newSource, null, newDestination);
        if(GlobalDebug.DEBUG){
            for(RouteItem item: subpathRouteItems){
                Log.d("MapGraph", item.toString());
            }
        }

        //note: items slide down when removed
        pathOfRouteItems.remove(pathOfRouteItems.get(currentPathIndex));
        pathOfRouteItems.remove(((isGoingBackwards)?pathOfRouteItems.get(currentPathIndex):pathOfRouteItems.get(currentPathIndex-1)));
//        if(false && currentPathIndex >= pathOfRouteItems.size()){
//            if(!pathOfRouteItems.addAll(subpathRouteItems)){
//                Log.e("MapGraph", "ERROR 1: Subpath Cannot Be Inserted!");
//            }
//        }else{
//            if(!pathOfRouteItems.addAll(((isGoingBackwards)?currentPathIndex:currentPathIndex-1), subpathRouteItems)){
//                Log.e("MapGraph", "ERROR 2: Subpath Cannot Be Inserted!");
//            }
//        }
        if(!pathOfRouteItems.addAll(((isGoingBackwards)?currentPathIndex:currentPathIndex-1), subpathRouteItems)){
            Log.e("MapGraph", "ERROR 2: Subpath Cannot Be Inserted!");
        }
    }

    public List<String> getCurrentDirections(){
        Integer originalIndex = currentPathIndex;
        currentPathIndex += (isGoingBackwards) ? 1: -1;
        Log.d("MapGraph", "[GetCurrentDirections] Original Index: " + originalIndex
                + "\tCurrent Index: " + currentPathIndex);
        List<String> result = (isGoingBackwards)?getPreviousDirections():getNextDirections();
        currentPathIndex = originalIndex;
        return result;
    }

    /**
     * This function returns the most recent path that was calculated
     *
     * @return a list of `RouteItem`s in order of the path to take
     * */
    public List<RouteItem> getRecentPath(){ return pathOfRouteItems; }

    /**
     * This function updates the brevity to the opposite value. For example, if the current state is
     * to have brief directions, then it will update to be the opposite value such that the current
     * state becomes detailed directions.
     * */
    public void updateDirectionsBrevity(){
        this.isBrief = !this.isBrief;
    }

    public List<RouteItem> getRemainingSubpathList(){//TODO:Test This
        int startIndex = (isGoingBackwards) ? currentPathIndex+1: currentPathIndex-1;
        int endIndex = pathOfRouteItems.size()-1;
        return pathOfRouteItems.subList(startIndex, endIndex);
    }

    public List<RouteItem> getVisitedSubpathList(){//TODO:Test This
        int startIndex = 0;
        int endIndex = (isGoingBackwards) ? currentPathIndex+1: currentPathIndex-1;
        return pathOfRouteItems.subList(startIndex, endIndex);
    }

    public Boolean isGoingBackwards(){ return isGoingBackwards; }

//    public boolean isFinishedRouteFlag() {
//        return finishedRouteFlag;
//    }
}
