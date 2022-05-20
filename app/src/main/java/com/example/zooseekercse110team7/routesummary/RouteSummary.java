package com.example.zooseekercse110team7.routesummary;

import com.example.zooseekercse110team7.map.CalculateShortestPath;
import com.example.zooseekercse110team7.map.IdentifiedWeightedEdge;
import com.example.zooseekercse110team7.planner.NodeItem;

import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.Collections;
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

    List<RouteItem> getItems() {
        return items;
    }

    public void setItems(List<RouteItem> items) {
        this.items = items;
    }
    public void updateRouteSummary(List<CalculateShortestPath> paths) {
        items.clear();
        for (CalculateShortestPath short_path: paths) {
            GraphPath path = short_path.getPath();
            if (path == null) {
                return;
            }

            List<Object> edges = path.getEdgeList();
            for (Object edge : edges) {
                String source = (String) path.getGraph().getEdgeSource(edge);
                String target = (String) path.getGraph().getEdgeTarget(edge);
                String distance = String.valueOf(path.getGraph().getEdgeWeight(edge));
                RouteItem routeItem = new RouteItem(target, source, distance);

                //check if source is already in list
                boolean containsSource = false;
                for (RouteItem item: items) {
                    if (item.fromExhibitName == source) { containsSource = true; }
                }
                if (!containsSource) {
                    items.add(routeItem);
                }
            }
        }
    }

    public RouteItem getRouteItem(int position) {
        return items.get(position);
    }
}
