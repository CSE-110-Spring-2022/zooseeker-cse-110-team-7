package com.example.zooseekercse110team7.map;

import android.util.Log;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

public class CalculateShortestPath {
    private String start;   // starting position id
    private String goal;    // end position id
    private AssetLoader g;  // Asset Loader which loads information from the JSON files
    GraphPath<String, IdentifiedWeightedEdge> path; // optimized path from position and information

    public CalculateShortestPath(String start, String goal, AssetLoader g){
        this.start = start;
        this.goal = goal;
        this.g = g;
        Log.d("ShortestPath", "Calculating with " +start+ "," +goal);
        this.path = DijkstraShortestPath.findPathBetween(this.g.graph, start, goal);
        Log.d("ShortestPath", "Path Calculated");
    }

    public String getShortestPath() {
        Log.d("ShortestPath", "Getting Shortest Path");
        int i = 1;
        String out = "From " + start + " to " + goal + ":\n";
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            //Conversion approximated to 5 decimal points and converted to int.
            double mToFt = 3.28084 * (g.graph.getEdgeWeight(e));
            int distInFeet = (int)mToFt;
            out += ("\t" + i + ". Proceed on " + g.eInfo.get(e.getId()).street + " " + distInFeet + " ft" + " to "
                    + g.vInfo.get(g.graph.getEdgeTarget(e).toString()).name + ".\n");

            i++;
            Log.d("ShortestPath", "|->["+String.valueOf(i)+"] Out: " + out);
        }

        out += "\n";

        Log.d("ShortestPath", "|-> Returning Out: " + out);

        return out;
    }

    public int getShortestDist(){
        Log.d("ShortestPath", "Calculating Shortest Distance");
        int total = 0;
        for(IdentifiedWeightedEdge e : path.getEdgeList()){
            /*what does 3.28084 mean? and what is `mToFt`?*/
            double mToFt = 3.28084 * (g.graph.getEdgeWeight(e));
            int distInFeet = (int)mToFt;
            total += distInFeet;
            Log.d("ShortestPath", "|-> Total: " + String.valueOf(total));
        }
        Log.d("ShortestPath", "|-> Returning Total: " + String.valueOf(total));
        return total;
    }
    public GraphPath getPath() {
        return path;
    }

}
