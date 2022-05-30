package com.example.zooseekercse110team7.map_v2;

import org.jgrapht.alg.util.Pair;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.nio.Attribute;


/**
 * Exactly like a DefaultWeightedEdge, but has an id field we
 * can use to look up the information about the edge with.
 */
public class IdentifiedWeightedEdge extends DefaultWeightedEdge {
    private String mySource, myTarget;        // keeps track of points based on context
    private boolean hasFlippedPoints = false; // boolean flag, determines if points flipped
    private String id = null;                 // ID of this edge

    /**
     * Flips the points of the edge and updates values `mySource` and `myTarget` to match.
     * */
    public void flipPoints(){
        this.hasFlippedPoints = true;

        //update ordering
        getEdgeSource();
        getEdgeTarget();
    }
    /**
     * Return edge's target ID based on the context (i.e flipped).
     *
     * @return a string representing the edge's target ID
     * */
    public String getEdgeTarget(){
        if(myTarget == null){
            String str = this.toString();
            int start = str.lastIndexOf(':');
            int end = str.lastIndexOf(')');
            myTarget = str.substring(start+2, end);
        }

        if(hasFlippedPoints){ return mySource; }

        return myTarget;
    }
    /**
     * Return edge's source ID based on the context (i.e flipped).
     *
     * @return a string representing the edge's source ID
     * */
    public String getEdgeSource(){
        if(mySource == null){
            String str = this.toString();
            int start = str.indexOf('(');
            int end = str.indexOf(' ');
            mySource = str.substring(start+1, end);
        }

        if(hasFlippedPoints){ return myTarget; }

        return mySource;
    }

    /* SELF EXPLANATORY FUNCTIONS */
    // ---------------------------------------START---------------------------------------------- //
    public Boolean isFlipped(){ return hasFlippedPoints; }
    public String getId() { return id; }
    public String getEdgeId(){ return getId(); }
    public void setId(String id) { this.id = id; }
    // ----------------------------------------END----------------------------------------------- //

    /**
     * Converts this object into a readable string in relation to the flipped points.
     *
     * @return a readable string about this object
     * */
    @Override
    public String toString() {
        return "(" + ((!hasFlippedPoints)?getSource():getTarget()) + " :" + id + ": " +
                ((!hasFlippedPoints)?getTarget():getSource()) + ")";
    }

    /**
     * This function is used a functor when parsing in the graph JSON file within the `ZooData`
     * class. It checks if an `IdentifiedWeightedEdge` is the same as the `attribute` object's value
     * so that we know the ID of an edge.
     *
     * @param pair a pair where the `IdentifiedWeightedEdge` is they key and the value is a String
     *             representing the ID of the edge
     * @param attr an Attribute object (which is a Map<Object, Object>) related to the edge
     * */
    public static void attributeConsumer(Pair<IdentifiedWeightedEdge, String> pair, Attribute attr) {
        IdentifiedWeightedEdge edge = pair.getFirst();
        String attrName = pair.getSecond();
        String attrValue = attr.getValue();

        if (attrName.equals("id")) {
            edge.setId(attrValue);
        }
    }
}

