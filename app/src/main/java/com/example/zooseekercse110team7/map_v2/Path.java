package com.example.zooseekercse110team7.map_v2;

import android.location.Location;
import android.util.Log;

import com.example.zooseekercse110team7.planner.NodeItem;
import com.example.zooseekercse110team7.routesummary.RouteItem;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a singleton class (so it can be called anywhere) that is related to finding the shortest
 * path. This class is mainly to just calculate the shortest path, but it can also return the cost
 * of the most recent calculated path.
 * */
public class Path {
    private static Path instance = new Path();
    private Path(){}
    public static Path getInstance(){
        return instance;
    }

    private MapGraph mapGraph = MapGraph.getInstance();
    private Double pathCost;

    /**
     * Returns a list of the edges (`IdentifiedWeightedEdge`) within *a* calculated path
     *
     * @param source the source of the path which is a vertex
     * @param destination the destination of the path which is a vertex
     *
     * @return list of `IdentifiedWeightedEdge`s between the two connecting vertices
     * */
    public List<IdentifiedWeightedEdge> getPathEdges(String source, String destination){
        GraphPath<String, IdentifiedWeightedEdge> path;
        path = DijkstraShortestPath.findPathBetween(mapGraph.getGraph(), source, destination);
        return path.getEdgeList();
    }

    /**
     * Retrieves the cost of the shortest path from a source and destination using Dijkstra.
     *
     * @param referencePoint point of reference which is used as the source
     * @param currentDestination destination for the path
     *
     * @return the cost of the path as a double
     * */
    private double getPathCost(String referencePoint, String currentDestination){
        GraphPath<String, IdentifiedWeightedEdge> path;
        path = DijkstraShortestPath.findPathBetween(mapGraph.getGraph(), referencePoint, currentDestination);
        return path.getWeight(); //sum of all weights leading to path
    }

    /**
     * Parses a list of `NodeItem`s into a list of Strings which are their respective `id`s
     *
     * @param nodeList a list of `NodeItem`s
     *
     * @return a list of Strings which are their respective `id`s
     * */
    private List<String> nodeListToStringList(List<NodeItem> nodeList){
        List<String> stringList = new ArrayList<>();
        if(nodeList == null){ return stringList; }
        for(NodeItem item: nodeList){
            stringList.add(((item.parent_id==null)?item.id: item.parent_id)); // exhibit or exhibit group
        }
        return stringList;
    }

    /**
     * Calculates the shortest path in part by using Dijkstra's Algorithm. For each item the user
     * wants to visit to, it calculates the shortest path from a reference point (like the entrance)
     * and finds the path that costs the least. That item is removed from the list of items that
     * need to be visited and the reference point is updated to be the exhibit/item that had the
     * least cost (i.e item that was just removed from the list). Repeat that until there are no
     * more items that need to be visited. Lastly, it finds the shortest path from the last
     * exhibit/item to be visited to the exit/destination.
     *
     * @param source vertex name to start path
     * @param mustVisitItems list of items that must be visited
     * @param destination vertex name of the last item to visit
     *
     * @return list of `RouteItem`s in order in which they should be visited for optimal path
     * */
    public List<RouteItem> getShortestPath(String source, List<NodeItem> mustVisitItems, String destination){
        List<String> remainingNames = nodeListToStringList(mustVisitItems);

        double totalCost = 0.0;
        List<RouteItem> route = new ArrayList<>();
        String bestItem = null;//will hold name of the item with the best cost in the current iteration
        String currentReferencePoint = source;
        double bestPathCost, costOfPath;

        /* CALCULATE PATHS */
        for(int i=0; mustVisitItems!=null && i < mustVisitItems.size(); i++){
            bestPathCost = Integer.MAX_VALUE;//set best cost to infinity
            for(int j=0; j < remainingNames.size(); j++){//for each remaining item
                costOfPath = getPathCost(currentReferencePoint, remainingNames.get(j)); // get cost from reference point to item
                if(costOfPath < bestPathCost) { //if cost is less than our current best cost
                    bestItem = remainingNames.get(j);//then get the name of item (that caused best cost)
                    bestPathCost = costOfPath;// then set the best cost cost to the current cost
                }//A WAY TO MAKE THIS MORE OPTIMAL IS CHECK WHICH PATH VISITS MORE NODES FOR THE SAME PRICE
            }//end of Sub Loop
            route.add(new RouteItem(bestItem, currentReferencePoint, Double.toString(bestPathCost))); // add item to ordered path
            currentReferencePoint = bestItem; // set reference point to best item (new starting position)
            totalCost += bestPathCost;// update total cost
            remainingNames.remove(bestItem); // remove item from the remaining items
        }//end of Main Loop

        /* CALCULATE PATH TO EXIT */
        //go to destination
        totalCost += bestPathCost = getPathCost(currentReferencePoint, destination);
        route.add(new RouteItem(destination, currentReferencePoint, Double.toString(bestPathCost)));
        //no need to update reference point as this is the last thing
        //we aren't looking at the remaining names list

        pathCost = totalCost;

        mapGraph.setPath(route);
        Log.w("Path", "WARNING! MapGraph's path is being updated -- function's usage has " +
                "side effects to the graph");

        return route;
    }
    /* Different signature, default source and default destination included when ran */
    public List<RouteItem> getShortestPath(List<NodeItem> mustVisitItems){
        String defaultSource = "entrance_exit_gate", defaultDestination = "entrance_exit_gate";//TODO: Check If There Is A Way NOT to hardcode this
        return this.getShortestPath(defaultSource, mustVisitItems, defaultDestination);
    }

    /**
     * Returns the total cost of a path last calculated
     *
     * @return null if no path has previously been calculated, else a Double of the total cost
     * */
    public Double getTotalCost(){ return pathCost; }

    //TODO
    public List<RouteItem> getShortestLocationPath(Location source, List<NodeItem> mustVisitItems, String destination){
        return null;
    }
}
