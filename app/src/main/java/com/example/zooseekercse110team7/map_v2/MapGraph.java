package com.example.zooseekercse110team7.map_v2;

import android.util.Log;

import com.example.zooseekercse110team7.GlobalDebug;
import com.example.zooseekercse110team7.planner.NodeItem;
import com.example.zooseekercse110team7.planner.UpdateNodeDaoRequest;
import com.example.zooseekercse110team7.routesummary.RouteItem;

import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.Collections;
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
    //  Singleton Setup
    // ---------------------------------------START---------------------------------------------- //
    public static MapGraph instance = new MapGraph();
    private MapGraph(){}
    public static MapGraph getInstance(){ return instance; }
    // ----------------------------------------END----------------------------------------------- //

    private AssetLoader assetLoader
            = AssetLoader.getInstance();        // an alias for `AssetLoader` instance
    private List<RouteItem> pathOfRouteItems;   // current path as a list of `RouteItem`s
    private Integer currentPathIndex = 0;       // current index of path item to look at
    private Boolean isGoingBackwards = false;   // boolean flag, determines if going backwards
    private Boolean isBrief = false;            // boolean flag, determines brief directions

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

    private String getCurrentSource(){
        return UserLocation.getInstance().getClosestVertex();
    }

    /**
     * This function acts as a singular step within an iteration of a list. Upon call, the current
     * index increases (if possible) and parses a list of `IdentifiedWeightedEdge` (edges) between
     * two exhibits/locations into strings.
     *
     * @return Directions in the form of a String list
     * */
    public List<String> getNextDirections(){
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
        result.add((getCurrentSource() + " -> " + currentRoute.getDestination() + "\n"));
        List<IdentifiedWeightedEdge> edges = Path
                .getInstance()
                .getPathEdges(getCurrentSource(), currentRoute.getDestination());
        if(!isBrief) {
            for (IdentifiedWeightedEdge edge : edges) {
                result.add(PrettyDirections
                        .getInstance()
                        .toPrettyDirectionsString(edge, getEdgeWeight(edge), previousEdge)
                );
                previousEdge = edge;
            }
        }else{
            result = PrettyDirections.getInstance().toPrettyBriefDirectionsString(edges);
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
        String newDestination = currentRoute.getSource();
        result.add((getCurrentSource() + " -> " + newDestination + "\n"));
        List<IdentifiedWeightedEdge> edges = Path
                .getInstance()
                .getPathEdges(getCurrentSource(), newDestination);
        if(!isBrief) {
            for (IdentifiedWeightedEdge edge : edges) {
                result.add(PrettyDirections
                        .getInstance()
                        .toPrettyDirectionsString(edge, getEdgeWeight(edge), previousEdge)
                );
                previousEdge = edge;
            }
        }else{
            result = PrettyDirections.getInstance().toPrettyBriefDirectionsString(edges);
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
        if(pathOfRouteItems.isEmpty() || 1 == pathOfRouteItems.size()
                || (isGoingBackwards && (currentPathIndex!=0) && currentPathIndex < 0)
                || (!isGoingBackwards && (currentPathIndex!=0) && currentPathIndex-1 < 0)){
            return null;
        }
        RouteItem currentRouteItem = pathOfRouteItems.get(
                (0 == currentPathIndex)?currentPathIndex:((isGoingBackwards)?currentPathIndex:currentPathIndex-1)
        );
        Log.d("MapGraph", "Current IDs: [Source] " + currentRouteItem.getSource() + "\t[Destination] "+ currentRouteItem.getDestination());
        Log.d("MapGraph","isGoingBackwards? [true]source : [false]destination -- Bool: " + isGoingBackwards.toString());

        String resultId = (isGoingBackwards)?currentRouteItem.getSource():currentRouteItem.getDestination();

        /* UPDATE APPROPRIATELY IF ID IS PARENT */
        if(UpdateNodeDaoRequest.getInstance().isParent(resultId)){
            List<NodeItem> children = UpdateNodeDaoRequest.getInstance().RequestChildrenOf(resultId);
            for(NodeItem child: children){
                if(child.onPlanner){
                    UpdateNodeDaoRequest.getInstance().RequestPlannerSkip(child.id);
                }
            }
        }

        return resultId;
    }

    /**
     * Updates the current path route `pathOfRouteItems` by removing the current items that the
     * user is being taken to. This is done by first removing the item with the list, then it finds
     * a new path by getting a new subpath of the remaining items to visit. The new path is based on
     * getting the shortest path from the current source and
     *  **NOT** connecting the current source to the next item in the list.
     *  The purpose of this class is to skip the current destination/exhibit.
     * */
    public void updatePathWithRemovedItem(){
        //save time by not calculating path
        //there is no need to update current path index as it will point to the next thing in the list
        //      and there are checks/safeguards in place
        if(1 == pathOfRouteItems.size() || pathOfRouteItems.isEmpty()){
            pathOfRouteItems = new ArrayList<>();
            return;
        }else if(isGoingBackwards){ //can assume at least 2 items
            if(0 == currentPathIndex){ return; } // can't skip at index -1

            //get previous source if it exists
            Log.d("RemovedItemPath", "Current: " + currentPathIndex + "\tArray Size: "
                    + pathOfRouteItems.size());
            if(currentPathIndex >= pathOfRouteItems.size()){
                currentPathIndex = pathOfRouteItems.size()-1;
            }
            Log.d("RemovedItemPath", "[After] Current: " + currentPathIndex
                    + "\tArray Size: "
                    + pathOfRouteItems.size());
            RouteItem previous = pathOfRouteItems.get(currentPathIndex-1);
            RouteItem current = pathOfRouteItems.get(currentPathIndex);
            //update current route item to match source and destination
            pathOfRouteItems.add(currentPathIndex, new RouteItem(
                    previous.getSource(),
                    current.getDestination(),
                    Double.toString(Path.getInstance().getPathCost(previous.getSource(), current.getDestination())) ));
            pathOfRouteItems.remove(previous);
            pathOfRouteItems.remove(current);
            return; } // https://piazza.com/class/l186r5pbwg2q4?cid=648
                                              // only delete item

        //can assume going forward
        /* GET SOURCE AND DESTINATION INFORMATION */
        String newSource = getCurrentSource();

        /* CALCULATE NEW SUBPATH */
        List<RouteItem> remainingList = getRemainingSubpathList();
        if(!remainingList.isEmpty()) { remainingList.remove(0); }
        List<RouteItem> subpathRouteItems = Path
                .getInstance()
                .notUpdateGraph()
                .getShortestPath(newSource, remainingList);
        if(GlobalDebug.DEBUG){
            for(RouteItem item: subpathRouteItems){
                Log.d("MapGraph", item.toString());
            }
        }

        /* REMOVE SKIPPED ITEMS FROM LIST */
        pathOfRouteItems.subList(currentPathIndex-1,  pathOfRouteItems.size()).clear();
//        pathOfRouteItems.remove(pathOfRouteItems.get(currentPathIndex));
//        pathOfRouteItems.remove((
//                (isGoingBackwards)
//                ?pathOfRouteItems.get(currentPathIndex)
//                :pathOfRouteItems.get(currentPathIndex-1)));

        /* APPEND SUBPATH TO END OF THE LIST */
        if(!pathOfRouteItems.addAll(currentPathIndex-1, subpathRouteItems)){
            Log.e("MapGraph", "ERROR 2: Subpath Cannot Be Inserted!");
        }
    }

    /**
     * Returns a list of Strings which are directions to the current exhibit/item to visit.
     *
     * @return a list of directions in the form of a string list
     * */
    public List<String> getCurrentDirections(){
        /* GET INDEX */
        Integer originalIndex = currentPathIndex;
        currentPathIndex += (0 == currentPathIndex)?currentPathIndex: ((isGoingBackwards) ? 1: -1);
        Log.d("MapGraph", "[GetCurrentDirections] Original Index: " + originalIndex
                + "\tCurrent Index: " + currentPathIndex);

        /* GET DIRECTIONS */
        List<String> result = (isGoingBackwards)?getPreviousDirections():getNextDirections();

        /* RESET INDEX TO ORIGINAL STATE */
        currentPathIndex = originalIndex;

        return result;
    }

    public void updatePath(){
        Path.getInstance().getShortestPath(UpdateNodeDaoRequest.getInstance().RequestPlannedItems());
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

    /**
     * Returns a list of the subpath that needs to be visited.
     *
     * @return a list of `RouteItem`s which is the subpath of unvisited items
     * */
    public List<RouteItem> getRemainingSubpathList(){//TODO:Test This
        if(pathOfRouteItems.size() <= 1 || currentPathIndex >= pathOfRouteItems.size()){
            return Collections.emptyList();
        }
        int startIndex = currentPathIndex;
        int endIndex = pathOfRouteItems.size();
        return new ArrayList<>(pathOfRouteItems.subList(startIndex, endIndex));
    }

    /**
     * Returns a list of the subpath that has been visited.
     *
     * @return a list of `RouteItem`s which is the subpath of visited items
     * */
    public List<RouteItem> getVisitedSubpathList(){//TODO:Test This
        if(pathOfRouteItems.size() <= 1 || currentPathIndex >= pathOfRouteItems.size()){ return Collections.emptyList();}
        int startIndex = 0;
        int endIndex = (0 == currentPathIndex)?currentPathIndex:((isGoingBackwards) ? currentPathIndex+1: currentPathIndex-1);
        return new ArrayList<>(pathOfRouteItems.subList(startIndex, endIndex));
    }

    /**
     * Checks if the user is currently going backwards/previous
     *
     * @return the current state of the user's progression (i.e forward or reverse)
     * */
    public Boolean isGoingBackwards(){ return isGoingBackwards; }
}
