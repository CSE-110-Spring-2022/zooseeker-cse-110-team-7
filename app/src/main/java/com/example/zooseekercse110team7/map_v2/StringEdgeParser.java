package com.example.zooseekercse110team7.map_v2;


import android.content.Context;
import android.util.Log;

import com.example.zooseekercse110team7.planner.NodeDatabase;
import com.example.zooseekercse110team7.planner.NodeItem;
import com.example.zooseekercse110team7.planner.ReadOnlyNodeDao;
import com.example.zooseekercse110team7.planner.UpdateNodeDaoRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Purpose of this class is to convert/parse `IdentifiedWeightedEdge` into "beautiful" Strings
 * */
public class StringEdgeParser {
    public static StringEdgeParser instance = new StringEdgeParser();
    private StringEdgeParser(){}
    public static StringEdgeParser getInstance(){ return instance; }

    private ReadOnlyNodeDao nodeDao;

    public StringEdgeParser setNodeDao(Context context){
        nodeDao = NodeDatabase.getSingleton(context).nodeDao();
        return this;
    }

    public StringEdgeParser setContext(Context context){
        nodeDao = NodeDatabase.getSingleton(context).nodeDao();
        return this;
    }

    public List<String> toPrettyBriefEdgeString(List<IdentifiedWeightedEdge> edges){
        String errorString = "[Path Error] Cannot Find Path Details!";
        List<String> result = new ArrayList<>();

        String lastStreet = "";
        String subDirection="";
        Double distance = 0.0;
        boolean gettingDestination = false;
        for(int i=0; i < edges.size(); i++){
            if(!lastStreet.equals(getStreet(edges.get(i))) || edges.size()-1 == i){
                if(!gettingDestination) {
                    subDirection += "Proceed on " + getStreet(edges.get(i)) + " for ";
                    gettingDestination = true;
                }else{
                    String anId = edges.get(i).getEdgeTarget();
                    VertexInfo n = AssetLoader.getInstance().getVertexMap().get(anId);
                    subDirection += distance.toString() + "ft to "
                            + getStreet(edges.get(i))
                            + "\n";
                    gettingDestination = false;
                    distance = 0.0;
                    result.add(subDirection);
                    subDirection = "";
                }
            }else{
                if(!gettingDestination) {
                    subDirection += "Proceed on " + getStreet(edges.get(i)) + " for ";
                    gettingDestination = true;
                }
            }
            distance += MapGraph.getInstance().getEdgeWeight(edges.get(i));
            lastStreet = getStreet(edges.get(i));
        }

        if(gettingDestination) {
            String anId = getLast(edges).getEdgeTarget();
            VertexInfo n = AssetLoader.getInstance().getVertexMap().get(anId);
            subDirection += distance.toString() + "ft to "
                    + n.name
                    + "\n";
            result.add(subDirection);
        }else{
            String anId = getLast(edges).getEdgeTarget();
            VertexInfo n = AssetLoader.getInstance().getVertexMap().get(anId);
            subDirection += "Proceed on " + getStreet(getLast(edges)) + " for "
                    + distance.toString() + "ft to " + n.name + "\n";
            result.add(subDirection);
        }

        return result;
    }

    public String toPrettyEdgeString(IdentifiedWeightedEdge edge, Double distance,
                                     IdentifiedWeightedEdge previousEdge){
        String errorString = "[Path Error] Cannot Find Path Details!";
        String result = "";
        String sourceId = edge.getEdgeSource();
        String destinationId = edge.getEdgeTarget();

        NodeItem sourceItem = nodeDao.get(sourceId);
        NodeItem destinationItem = nodeDao.get(destinationId);

        if(sourceItem == null || destinationItem == null){
            Log.e("StringEdgeParser", "[ERROR] Source NodeItem or Destination NodeItem is "
                    + "NULL!");
            return errorString;
        }


        String previousStreet = (previousEdge != null)?getStreet(previousEdge):"";
        String currentStreet = getStreet(edge);
        boolean sourceIsStreet = sourceItem.name.contains("/");
        boolean targetIsStreet = destinationItem.name.contains("/");
        if(sourceIsStreet && targetIsStreet){
            List<String> streetSpliceSource = Arrays.asList(
                    nodeDao.get(edge.getEdgeSource()).name.split(" / ")
            );
            List<String> streetSpliceTarget = Arrays.asList(
                    nodeDao.get(edge.getEdgeTarget()).name.split(" / ")
            );
            if(!edge.isFlipped()) {
                if (Objects.equals(getFirst(streetSpliceSource), getFirst(streetSpliceTarget))) {
                    result = "Continue on " + getFirst(streetSpliceTarget) + " for "
                            + distance.toString() + "ft towards " + getLast(streetSpliceTarget)
                            + "\n";
                } else if (Objects.equals(getLast(streetSpliceSource), getFirst(streetSpliceTarget))) {
                    result = "Proceed on " + getLast(streetSpliceSource) + " for "
                            + distance.toString() + "ft towards " + getLast(streetSpliceTarget)
                            + "\n";
                }
            }else{
                if (Objects.equals(getFirst(streetSpliceSource), getFirst(streetSpliceTarget))) {
                    if(previousStreet.equals(currentStreet)){
                        result = "Continue on " + getFirst(streetSpliceTarget) + " for "
                                + distance.toString() + "ft towards " + getLast(streetSpliceTarget)
                                + "\n";
                    }else{
                        result = "Proceed on " + getFirst(streetSpliceTarget) + " for "
                                + distance.toString() + "ft towards " + getLast(streetSpliceTarget)
                                + "\n";
                    }
                } else if (Objects.equals(getFirst(streetSpliceSource), getLast(streetSpliceTarget))) {
                    if(previousStreet.equals(currentStreet)){
                        result = "Continue on " + getLast(streetSpliceTarget) + " for "
                                + distance.toString() + "ft towards " + getFirst(streetSpliceTarget)
                                + "\n";
                    }else{
                        result = "Proceed on " + getLast(streetSpliceSource) + " for "
                                + distance.toString() + "ft towards " + getFirst(streetSpliceTarget)
                                + "\n";
                    }
                }
            }
        }else if(sourceIsStreet || !targetIsStreet){
            result = "Proceed on " + getStreet(edge) + " for " + distance.toString() + "ft towards "
                    + destinationItem.name+ "\n";//check if parent
        }else if(targetIsStreet){
            result = "Proceed on " + getStreet(edge) + " for " + distance.toString() + "ft towards "
                    + destinationItem.name+ "\n";//check if parent
        }else{
            // it should never get to this point as the second nested conditional takes care of
            // it. If it ever gets to this point then some edge case was not accounted for!
            Log.e("StringEdgeParser", "Found Edge Case!");
        }

        return result;
    }

    public String toPrettyEdgeStringReverse(IdentifiedWeightedEdge edge, Double distance,
                                            IdentifiedWeightedEdge previousEdge){
        String errorString = "[Path Error] Cannot Find Path Details!";
        String result="";
        String sourceId = edge.getEdgeSource();
        String destinationId = edge.getEdgeTarget();


        //Note: the source and destination are based on reversed order, so you can assume everything
        // is B to A (reverse) and not A to B (forward)
        NodeItem sourceItem = nodeDao.get(destinationId);
        NodeItem destinationItem = nodeDao.get(sourceId);

        if(sourceItem == null || destinationItem == null){
            Log.e("StringEdgeParser", "[ERROR] Source NodeItem or Destination NodeItem is "
                    +"NULL!");
            return errorString;
        }

        String previousStreet = (previousEdge != null)?getStreet(previousEdge):"";
        String currentStreet = getStreet(edge);
        boolean sourceIsStreet = sourceItem.name.contains("/");
        boolean targetIsStreet = destinationItem.name.contains("/");
        if(sourceIsStreet && targetIsStreet){
            List<String> streetSpliceSource = Arrays.asList(
                    nodeDao.get(edge.getEdgeSource()).name.split(" / ")
            );
            List<String> streetSpliceTarget = Arrays.asList(
                    nodeDao.get(edge.getEdgeTarget()).name.split(" / ")
            );
            if(!edge.isFlipped()) {
                if (Objects.equals(getFirst(streetSpliceSource), getFirst(streetSpliceTarget))) {
                    result = "Continue on " + getFirst(streetSpliceTarget) + " for "
                            + distance.toString() + "ft towards " + getLast(streetSpliceTarget)
                            + "\n";
                } else if (Objects.equals(getLast(streetSpliceSource), getFirst(streetSpliceTarget))) {
                    result = "Proceed on " + getLast(streetSpliceSource) + " for "
                            + distance.toString() + "ft towards " + getLast(streetSpliceTarget)
                            + "\n";
                }
            }else{
                if (Objects.equals(getFirst(streetSpliceSource), getFirst(streetSpliceTarget))) {
                    if(previousStreet.equals(currentStreet)){
                        result = "Continue on " + getFirst(streetSpliceTarget) + " for "
                                + distance.toString() + "ft towards " + getLast(streetSpliceTarget)
                                + "\n";
                    }else{
                        result = "Proceed on " + getFirst(streetSpliceTarget) + " for "
                                + distance.toString() + "ft towards " + getLast(streetSpliceTarget)
                                + "\n";
                    }
                } else if (Objects.equals(getFirst(streetSpliceSource), getLast(streetSpliceTarget))) {
                    if(previousStreet.equals(currentStreet)){
                        result = "Continue on " + getLast(streetSpliceTarget) + " for "
                                + distance.toString() + "ft towards " + getFirst(streetSpliceTarget)
                                + "\n";
                    }else{
                        result = "Proceed on " + getLast(streetSpliceSource) + " for "
                                + distance.toString() + "ft towards " + getFirst(streetSpliceTarget)
                                + "\n";
                    }
                }
            }
        }else if(sourceIsStreet || !targetIsStreet){
            result = "Proceed on " + getStreet(edge) + " for " + distance.toString() + "ft towards "
                    + destinationItem.name+ "\n";//check if parent
        }else if(targetIsStreet){
            result = "Proceed on " + getStreet(edge) + " for " + distance.toString() + "ft towards "
                    + destinationItem.name+ "\n";//check if parent
        }else{
            // it should never get to this point as the second nested conditional takes care of
            // it. If it ever gets to this point then some edge case was not accounted for!
            Log.e("StringEdgeParser", "Found Edge Case!");
        }

        return result;
    }

    private String getStreet(IdentifiedWeightedEdge edge){
        String street = AssetLoader.getInstance().getEdgeMap().get(edge.getEdgeId()).street;
        if(street == null){
            return "NULL";
        }

        return street;
    }

    private <T> T getLast(List<T> aList){
        return aList.get(aList.size()-1);
    }

    private <T> T getFirst(List<T> aList){
        Optional<T> itemOpt = aList.stream().findFirst();
        if(!itemOpt.isPresent()){
            return null;
        }
        return itemOpt.get();
    }
}
