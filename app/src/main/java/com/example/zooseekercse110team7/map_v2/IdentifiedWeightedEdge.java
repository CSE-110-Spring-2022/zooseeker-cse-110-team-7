package com.example.zooseekercse110team7.map_v2;

import org.jgrapht.alg.util.Pair;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.nio.Attribute;

/**
 * Exactly like a DefaultWeightedEdge, but has an id field we
 * can use to look up the information about the edge with.
 */
public class IdentifiedWeightedEdge extends DefaultWeightedEdge {

    private String id = null;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    @Override
    public String toString() {
        return "(" + getSource() + " :" + id + ": " + getTarget() + ")";
    }

    public static void attributeConsumer(Pair<IdentifiedWeightedEdge, String> pair, Attribute attr) {
        IdentifiedWeightedEdge edge = pair.getFirst();
        String attrName = pair.getSecond();
        String attrValue = attr.getValue();

        if (attrName.equals("id")) {
            edge.setId(attrValue);
        }
    }

    public String getEdgeTarget(){
        String str = this.toString();
        int start = str.lastIndexOf(':');
        int end = str.lastIndexOf(')');
        return str.substring(start+2, end);
    }
    public String getEdgeSource(){
        String str = this.toString();
        int start = str.indexOf('(');
        int end = str.indexOf(' ');
        return str.substring(start+1, end);
    }
}

