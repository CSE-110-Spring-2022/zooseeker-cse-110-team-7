package com.example.zooseekercse110team7.routesummary;

import com.example.zooseekercse110team7.map.IdentifiedWeightedEdge;
import com.example.zooseekercse110team7.planner.NodeItem;

import org.jgrapht.GraphPath;

/**
 * An itermediary class used by the Planner
 * */
public class RouteItem {
    String toExhibitName, fromExhibitName, distance;
    public RouteItem(String dest, String source, String dist){//TODO: change order -> source, dest, dist
        this.toExhibitName = dest;
        this.fromExhibitName = source;
        this.distance = dist;
    }

    @Override
    public String toString() {
        return "RouteItem{" +
                "toExhibitName='" + toExhibitName + '\'' +
                ", fromExhibitName='" + fromExhibitName + '\'' +
                ", distance='" + distance + '\'' +
                '}';
    }
}
