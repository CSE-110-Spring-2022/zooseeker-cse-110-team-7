package com.example.zooseekercse110team7.routesummary;


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

    public String getSource(){ return fromExhibitName; }
    public String getDestination(){ return toExhibitName; }
}
