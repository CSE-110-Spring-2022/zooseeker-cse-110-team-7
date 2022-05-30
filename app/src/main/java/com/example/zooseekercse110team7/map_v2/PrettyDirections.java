package com.example.zooseekercse110team7.map_v2;


import android.content.Context;
import android.util.Log;

import com.example.zooseekercse110team7.planner.NodeDatabase;
import com.example.zooseekercse110team7.planner.NodeItem;
import com.example.zooseekercse110team7.planner.ReadOnlyNodeDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Purpose of this class is to convert/parse edges from a graph (which are connected as a path)
 * into "beautiful" Strings for the user to read. This is a singleton class and can be called just
 * about anywhere in the program. This class uses a `ReadOnlyNodeDao` to retrieve any information
 * from the database about nodes/vertices. The reason for singleton pattern is allow for
 * scalability, mainly that of the Route Summary. Moreover, it can be effective for debugging.
 * */
public class PrettyDirections {
    //  Singleton Setup
    // ---------------------------------------START---------------------------------------------- //
    public static PrettyDirections instance = new PrettyDirections();
    private PrettyDirections(){}
    public static PrettyDirections getInstance(){ return instance; }
    // ----------------------------------------END----------------------------------------------- //

    private ReadOnlyNodeDao nodeDao; // a dao to only read information from the database

    /**
     * Sets the context for the NodeDao to setup and use/reference the database. The Dao only reads
     * information from the database in this class.
     *
     * @param context the context in which the `nodeDao` is set
     *
     * @return this instance of the object -- allows for chain of command
     * */
    public PrettyDirections setNodeDao(Context context){
        nodeDao = NodeDatabase.getSingleton(context).nodeDao();
        return this;
    }

    /**
     * Sets the context for the NodeDao to setup and use/reference the database. The Dao only reads
     * information from the database in this class. Moreover, it can update the context for the
     * NodeDao.
     *
     * @param context the context in which the `nodeDao` is set
     *
     * @return this instance of the object -- allows for chain of command
     * */
    public PrettyDirections setContext(Context context){
        nodeDao = NodeDatabase.getSingleton(context).nodeDao();
        return this;
    }

    /**
     * Returns a list of brief directions between a list of ordered edges. The directions only call
     * out a landmark only when turning on to a new street/trail.
     *
     * @param edges a list of edges which are ordered in terms of visitation & have Sources and
     *              Targets based on the context it's called -- this can cause issues
     *
     * @return a list of Strings that are brief directions
     * */
    public List<String> toPrettyBriefDirectionsString(List<IdentifiedWeightedEdge> edges){
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

    /**
     * Returns a string of directions from an edge's Source and Target. This assumes that ordering
     * of an edges Source and Target is as expect (i.e A->B will not be the same result as B->A).
     *
     * @param edge an edge within the graph
     * @param distance the distance for the edge
     * @param previousEdge the previous edge that connects to current edge -- can be null
     *
     * @return a String the has all the directions regarding the edge
     * */
    public String toPrettyDirectionsString(IdentifiedWeightedEdge edge, Double distance,
                                     IdentifiedWeightedEdge previousEdge){
        String errorString = "[Path Error] Cannot Find Path Details!";
        String result = "";

        String sourceId = edge.getEdgeSource();
        String destinationId = edge.getEdgeTarget();
        NodeItem sourceItem = nodeDao.get(sourceId);
        NodeItem destinationItem = nodeDao.get(destinationId);

        /* CHECK IF IT'S ITEMS EXIST */
        if(sourceItem == null || destinationItem == null){
            Log.e("StringEdgeParser", "[ERROR] Source NodeItem or Destination NodeItem is "
                    + "NULL!");
            return errorString;
        }


        /* GET DIRECTION */
        String previousStreet = (previousEdge != null)?getStreet(previousEdge):"";
        String currentStreet = getStreet(edge);
        boolean sourceIsStreet = sourceItem.name.contains("/");
        boolean targetIsStreet = destinationItem.name.contains("/");
        if(sourceIsStreet && targetIsStreet){
            //name of the streets parsed as an array "A / B" -> ["A", "B"]
            List<String> streetSpliceSource = Arrays.asList(
                    nodeDao.get(edge.getEdgeSource()).name.split(" / ")
            );
            List<String> streetSpliceTarget = Arrays.asList(
                    nodeDao.get(edge.getEdgeTarget()).name.split(" / ")
            );

            if(!edge.isFlipped()) { // check if edge's source and target were flipped
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
                //check if street names are the same
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
                } //check if names of streets are related
                else if (Objects.equals(getFirst(streetSpliceSource), getLast(streetSpliceTarget))) {
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

    /**
     * Returns the name of the street the edge is associated with.
     *
     * @param edge an edge which contains a respective Source and Target based on the context of the
     *            call
     *
     * @return a string which refers to the name of the street, empty string if no street
     * */
    private String getStreet(IdentifiedWeightedEdge edge){
        String street = AssetLoader.getInstance().getEdgeMap().get(edge.getEdgeId()).street;

        if(street == null){
            return "";
        }

        return street;
    }

    /**
     * Get's the last item of any list. The main reason this is created is for readability.
     *
     * @param aList some list of type a generic type
     *
     * @return a generic object of type `T`, an object contained within the list
     * */
    private <T> T getLast(List<T> aList){
        return aList.get(aList.size()-1);
    }

    /**
     * Get's the first item of any list. The main reason this is created is for readability.
     *
     * @param aList some list of type a generic type
     *
     * @return a generic object of type `T`, an object contained within the list
     * */
    private <T> T getFirst(List<T> aList){
        Optional<T> itemOpt = aList.stream().findFirst();
        if(!itemOpt.isPresent()){
            return null;
        }
        return itemOpt.get();
    }
}
