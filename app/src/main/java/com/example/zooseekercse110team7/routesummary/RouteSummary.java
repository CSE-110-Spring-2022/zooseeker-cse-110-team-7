package com.example.zooseekercse110team7.routesummary;

import com.example.zooseekercse110team7.map_v2.Path;
import com.example.zooseekercse110team7.planner.NodeItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class that contains list of route items
 * */
public class RouteSummary {
    private List<RouteItem> items = new ArrayList<>();

    private static RouteSummary instance = new RouteSummary();

    private RouteSummary() {
    }
    public static RouteSummary getInstance(){
        return instance;
    }

    public List<RouteItem> getItems() {
        return items;
    }

    public void setItems(List<RouteItem> items) {
        this.items = items;
    }

    public void updateRouteSummary(List<NodeItem> mustVisitItems) {
        items = Path.getInstance().getShortestPath(mustVisitItems);
    }

    public RouteItem getRouteItem(int position) {
        return items.get(position);
    }
}
