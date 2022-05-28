package com.example.zooseekercse110team7.map_v2;


/**
 * Purpose of this class is to convert/parse `IdentifiedWeightedEdge` into "beautiful" Strings
 * */
public class StringEdgeParser {
    public static StringEdgeParser instance = new StringEdgeParser();
    private StringEdgeParser(){}
    public static StringEdgeParser getInstance(){ return instance; }
}
