package com.example.zooseekercse110team7.map_v2;

import android.util.Log;

import com.example.zooseekercse110team7.map.IdentifiedWeightedEdge;
import com.example.zooseekercse110team7.planner.NodeItem;
import com.example.zooseekercse110team7.routesummary.RouteItem;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * https://www.baeldung.com/cs/shortest-path-to-nodes-graph
 * */
public class Path {
    private final MapGraph mapGraph;
    private Double pathCost;

    public Path(MapGraph mapGraph){ this.mapGraph = mapGraph; }

    private double getPathCost(String refrencePoint, String currentDestination){
        GraphPath<String, IdentifiedWeightedEdge> path;
        path = DijkstraShortestPath.findPathBetween(mapGraph.getGraph(), refrencePoint, currentDestination);
        return path.getWeight(); //sum of all weights leading to path
    }

    private List<String> nodeListToStringList(List<NodeItem> nodeList){
        List<String> stringList = new ArrayList<>();
        for(NodeItem item: nodeList){
            stringList.add(item.id);
        }
        return stringList;
    }

    //https://stackoverflow.com/questions/222413/find-the-shortest-path-in-a-graph-which-visits-certain-nodes
    public List<RouteItem> getShortestPath(String source, List<NodeItem> mustVisitItems, String destination){
        List<String> remainingNames = nodeListToStringList(mustVisitItems);

        double totalCost = 0.0;
        List<RouteItem> route = new ArrayList<>();
        String bestItem = null;//will hold name of the item with the best cost in the current iteration
        String currentReferencePoint = source;
        double bestPathCost, costOfPath;
        for(int i=0; i < mustVisitItems.size(); i++){
            bestPathCost = Integer.MAX_VALUE;//set best cost to infinity
            for(int j=0; j < remainingNames.size(); j++){//for each remaining item
                costOfPath = getPathCost(currentReferencePoint, remainingNames.get(i)); // get cost from reference point to item
                if(costOfPath < bestPathCost) { //if cost is less than our current best cost
                    bestItem = remainingNames.get(i);//get the name of item (that caused best cost)
                    bestPathCost = costOfPath;//set the best cost cost to the current cost
                }
            }//end of Sub Loop
            route.add(new RouteItem(bestItem, currentReferencePoint, Double.toString(bestPathCost))); // add item to ordered path
            currentReferencePoint = bestItem; // set reference point to best item (new starting position)
            totalCost += bestPathCost;// update total cost
            remainingNames.remove(bestItem); // remove item from the remaining items
        }//end of Main Loop

        pathCost = totalCost;

        return route;
    }

}
