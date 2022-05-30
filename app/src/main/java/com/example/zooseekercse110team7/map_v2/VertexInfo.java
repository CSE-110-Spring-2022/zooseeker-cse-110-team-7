package com.example.zooseekercse110team7.map_v2;


import androidx.annotation.VisibleForTesting;

import com.example.zooseekercse110team7.planner.NodeItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VertexInfo {
//    public enum Kind {
//        // The SerializedName annotation tells GSON how to convert
//        // from the strings in our JSON to this Enum.
//        @SerializedName("gate") GATE,
//        @SerializedName("exhibit") EXHIBIT,
//        @SerializedName("intersection") INTERSECTION
//    }

    public String id;
    public String parent_id;
    public String kind;
    public String name;
    public List<String> tags;
    double lat;
    double lng;

    @VisibleForTesting
    public NodeItem toNodeItem(){
        return new NodeItem(id, parent_id, kind, name, tags, lat, lng);
    }
}
