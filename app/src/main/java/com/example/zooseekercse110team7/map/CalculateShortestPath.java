package com.example.zooseekercse110team7.map;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

public class CalculateShortestPath {

    private String start;
    private String goal;
    private AssetLoader g;
    GraphPath<String, IdentifiedWeightedEdge> path;

    public CalculateShortestPath(String start, String goal, AssetLoader g){
        this.start = start;
        this.goal = goal;
        this.g = g;
        this.path = DijkstraShortestPath.findPathBetween(this.g.graph, start, goal);
    }

    public void printShortestPath(){
        System.out.printf("The shortest path from '%s' to '%s' is:\n", start, goal);

        int i = 1;
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            System.out.printf("  %d. Walk %.0f meters along %s from '%s' to '%s'.\n",
                    i,
                    g.graph.getEdgeWeight(e),
                    g.eInfo.get(e.getId()).street,
                    g.vInfo.get(g.graph.getEdgeSource(e).toString()).name,
                    g.vInfo.get(g.graph.getEdgeTarget(e).toString()).name);
            i++;
        }
    }

    public String getShortestPath() {
        int i = 1;
        String out = "";
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            //Conversion approximated to 5 decimal points and converted to int.
            double mToFt = 3.28084 * (g.graph.getEdgeWeight(e));
            int distInFeet = (int)mToFt;
            out += ("Proceed on " + g.eInfo.get(e.getId()).street + " " + distInFeet + " ft towards " + g.vInfo.get(g.graph.getEdgeTarget(e).toString()).name + ".\n");

            i++;
        }

        return out;
    }

}
