package com.example.zooseekercse110team7.map_v2;


import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * This class holds information regarding a vertex/node within the graph.
 * */
public class VertexInfo {
    public enum Kind {
        // The SerializedName annotation tells GSON how to convert
        // from the strings in our JSON to this Enum.
        @SerializedName("gate") GATE,
        @SerializedName("exhibit") EXHIBIT,
        @SerializedName("intersection") INTERSECTION
    }

    public String id;           // unique ID associated with the node
    public String parent_id;    // @Nullable - unique parent ID associated with the node you can
                                // assume this node is a child if this field has a value
    public Kind kind;           // the category this node is associated with
    public String name;         // name about the node
    public List<String> tags;   // tags associated with the node
    double lat;                 // latitude location
    double lng;                 // longitude location
}
