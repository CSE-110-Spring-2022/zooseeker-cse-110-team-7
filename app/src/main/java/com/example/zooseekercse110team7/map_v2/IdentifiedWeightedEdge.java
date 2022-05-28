package com.example.zooseekercse110team7.map_v2;

import org.jgrapht.alg.util.Pair;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.nio.Attribute;

/**
 * Exactly like a DefaultWeightedEdge, but has an id field we
 * can use to look up the information about the edge with.
 */
public class IdentifiedWeightedEdge extends DefaultWeightedEdge {

    private boolean hasFlippedPoints = false;
    private String mySource, myTarget;

    private String id = null;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    @Override
    public String toString() {
        return "(" + ((!hasFlippedPoints)?getSource():getTarget()) + " :" + id + ": " +
                ((!hasFlippedPoints)?getTarget():getSource()) + ")";
    }

    public static void attributeConsumer(Pair<IdentifiedWeightedEdge, String> pair, Attribute attr) {
        IdentifiedWeightedEdge edge = pair.getFirst();
        String attrName = pair.getSecond();
        String attrValue = attr.getValue();

        if (attrName.equals("id")) {
            edge.setId(attrValue);
        }
    }

    public void flipPoints(){ this.hasFlippedPoints = true; }

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

    public String getEdgeId(){ return id; }

    public Boolean isFlipped(){ return hasFlippedPoints; }
}

