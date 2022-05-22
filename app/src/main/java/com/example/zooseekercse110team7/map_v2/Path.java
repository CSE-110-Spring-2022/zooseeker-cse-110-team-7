package com.example.zooseekercse110team7.map_v2;

import android.util.Log;

import com.example.zooseekercse110team7.map.IdentifiedWeightedEdge;
import com.example.zooseekercse110team7.planner.NodeItem;
import com.example.zooseekercse110team7.routesummary.RouteItem;

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
    Graph graph;
    public Path(Graph graph){ this.graph = graph; }

    public void calculatePath(String source, List<NodeItem> mustVisitItems, String destination){

//        Log.d("Path", "Starting Calculations");
        List<String> nodeNameList = new ArrayList<>();
        nodeNameList.add(source);
        for(NodeItem item: mustVisitItems){
            nodeNameList.add(item.id);
        }
        nodeNameList.add(destination);

//        Set<IdentifiedWeightedEdge> neighbors;
//        for(String vertex: nodeNameList){
//            neighbors = graph.getEdges(vertex);
//            for(IdentifiedWeightedEdge neighbor: neighbors){
//
//            }
//        }
//
//        Log.d("Path", "Creating Map");
//        Map<String, Map<String, Double>> vtDistances = new HashMap<String, Map<String, Double>>();
//        for(String item: nodeNameList){
//            vtDistances.put(item, graph.getNeighborsMap(item));
//        }
//
//        Log.d("Path", "Creating TSP");
//        TSP tsp = new TSP(vtDistances);
//        String[] aPath = tsp.findShortestPath();
//
//        Log.d("Path", "Resulting Path: ");
//        for(String routeItem: aPath){
//            Log.d("Path", routeItem);
//        }
    }

    private class Node{
        long x, y;
        int nodeId;
        String name;
        int bitmask;
        double cost;
        public Node(){}
        public Node(String name, int bitmask, int cost){
            this.name = name;
            this.bitmask = bitmask;
            this.cost = cost;
        }
        public Node(String name, int x, int y, int nodeId){
            this.name = name;
            this.x = x;
            this.y = y;
            this.nodeId = nodeId;
            this.cost = 0.0;
        }
        public Node(String name, int nodeId){
            this.name = name;
            this.nodeId = nodeId;
        }
    }
//
    //https://stackoverflow.com/questions/222413/find-the-shortest-path-in-a-graph-which-visits-certain-nodes
    public List<RouteItem> getShortestPath(String source, List<NodeItem> mustVisitItems, String destination){
        List<String> nodeNameList = new ArrayList<>();
        //nodeNameList.add(source);
        for(NodeItem item: mustVisitItems){
            nodeNameList.add(item.id);
        }
        //nodeNameList.add(destination);
        int nodeId = 0;
        int matrixLength = graph.getMatrixSize(); // length of matrix is number of items to visit PLUS start and end position
        int[][] distance = new int[matrixLength][matrixLength];

        //SET VALUES TO INFINITY
        for(int i=0; i < matrixLength; i++){
            for(int j=0; j < matrixLength; j++){
                distance[i][j] = Integer.MAX_VALUE;
            }
        }

//        Node[] nodeList = new Node[matrixLength];
//        for(int i=0; i < matrixLength; i++){
//            nodeList[i] = new Node(nodeNameList.get(i), nodeId); nodeId++;
//        }
//
//        //Set<IdentifiedWeightedEdge> edges = graph.getEdges(source);
//        Node currentNodeEdge;
//        for(Node node: nodeList){
//            Set<IdentifiedWeightedEdge> edges = graph.getEdges(source);
//            for(IdentifiedWeightedEdge edge: edges){
//                currentNodeEdge = Arrays.asList(nodeList)
//                        .stream()
//                        .filter(aNode -> edge.getName().equals(aNode.name))
//                        .findFirst();
//                distance[node.nodeId][currentNodeEdge.nodeId]
//                        = graph.getEdgeWeight(node.name, currentNodeEdge.name);
//            }
//        }
//
//
//        //PRE COMPUTATION
//        for(int i=1; i <= matrixLength; i ++){
//            for(int j=1; j <= matrixLength; j++){
//                for(int k=1; k <= matrixLength; k++){
//                    distance[j][k] = Math.min(distance[j][k], distance[j][i] + distance[i][k]);
//                }
//            }
//        }

//        double shortest = Double.POSITIVE_INFINITY;
//        Set<IdentifiedWeightedEdge> edges = graph.getEdges(source);
//        shortest = Math.min(shortest, )

        PriorityQueue<Node> queue = new PriorityQueue<>(matrixLength, new Comparator<Node>() {
            @Override
            public int compare(Node n1, Node n2) {
                return (int)(n1.cost -n2.cost);//return lowest cost/edge weight
                //Note: rounding because comparator requires an int
            }
        });


        Node u = new Node(source, 0,0);
        queue.add(u);
        distance[graph.getId(u.name)][0] = 0;
        while(!queue.isEmpty()){
            u = queue.poll();

            if(u.cost != distance[graph.getId(u.name)][u.bitmask]){ continue; }
            Set<IdentifiedWeightedEdge> neighbors = graph.getEdges(u.name);
            for(IdentifiedWeightedEdge neighbor: neighbors){
                int newBitmask = u.bitmask;
                if(neighborInList(graph.getNeighborName(neighbor,u.name),nodeNameList)){
                    int vid = graph.getId(graph.getNeighborName(neighbor, u.name));
                    newBitmask |= (1 << vid);
                }
                int newCost = (int)Math.round(u.cost) + (int)Math.round(graph.getEdgeWeight(u.name, graph.getNeighborName(neighbor,u.name)));
                if(newCost < distance[graph.getId(graph.getNeighborName(neighbor,u.name))][newBitmask]){
                    distance[graph.getId(graph.getNeighborName(neighbor,u.name))][newBitmask] = newCost;
                    queue.add(new Node(graph.getNeighborName(neighbor,u.name), newBitmask, newCost));
                }
            }
        }

        int bitmaskResult = distance[graph.getId(destination)][(1<<mustVisitItems.size()) - 1];
        String result = Integer.toBinaryString(bitmaskResult);
        String resultWithPadding = String.format("%7s", result).replaceAll(" ", "0");  // 32-bit Integer
        Map<String, Integer> idMap = graph.getIdMap();
        return null;

        //if it works then we can run the algorithim again, but now from last item in planner to the exit
    }

    private boolean neighborInList(String neighbor, List<String> nodeNameList){
        return nodeNameList.contains(neighbor);
    }

    private void setBitmask(int distance[][], int index, int bitMask, int bitLength){
        String bits = Integer.toBinaryString(bitMask);
        String bitString = String.format("%32s", bits).replaceAll(" ", "0");  // 32-bit Integer
        int bitmaskLength = distance[index].length;
        int offset = bitString.length() - bitLength;
        for(int i=0; i<bitmaskLength; i++){
            distance[index][i] = Integer.parseInt(String.valueOf(bitString.charAt(offset+i)));
        }
    }

}
