package com.example.zooseekercse110team7.map_v2;


import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.nio.json.JSONImporter;

public class ZooData {



    public static Map<String, VertexInfo> loadVertexInfoJSON(Context aContext,String path) {
        Map<String, VertexInfo> indexedZooData = new HashMap<>();
        try{
            InputStream inputStream = aContext.getAssets().open(path);
            Reader reader = new InputStreamReader(inputStream);
            Gson gson = new Gson();
            Type type = new TypeToken<List<VertexInfo>>(){}.getType();
            List<VertexInfo> zooData = gson.fromJson(reader, type);

            // This code is equivalent to:
            //
            // Map<String, ZooData.VertexInfo> indexedZooData = new HashMap();
            // for (ZooData.VertexInfo datum : zooData) {
            //   indexedZooData[datum.id] = datum;
            // }
            //
            indexedZooData = zooData
                    .stream()
                    .collect(Collectors.toMap(v -> v.id, datum -> datum));


        } catch (NullPointerException | IOException e){
            e.printStackTrace();
        }
        return indexedZooData;
    }

    public static Map<String, EdgeInfo> loadEdgeInfoJSON(Context aContext, String path) {
        Map<String, EdgeInfo> indexedZooData = new HashMap<>();
        try{
            InputStream inputStream = aContext.getAssets().open(path);
            Reader reader = new InputStreamReader(inputStream);

            Gson gson = new Gson();
            Type type = new TypeToken<List<EdgeInfo>>(){}.getType();
            List<EdgeInfo> zooData = gson.fromJson(reader, type);

            indexedZooData = zooData
                    .stream()
                    .collect(Collectors.toMap(v -> v.id, datum -> datum));
        }catch (IOException e){
            e.printStackTrace();
        }


        return indexedZooData;
    }

    public static Graph<String, IdentifiedWeightedEdge> loadZooGraphJSON(Context context, String path) {
        // Create an empty graph to populate.
        Graph<String, IdentifiedWeightedEdge> g = new DefaultUndirectedWeightedGraph<>(IdentifiedWeightedEdge.class);

        // Create an importer that can be used to populate our empty graph.
        JSONImporter<String, IdentifiedWeightedEdge> importer = new JSONImporter<>();

        // We don't need to convert the vertices in the graph, so we return them as is.
        importer.setVertexFactory(v -> v);

        // We need to make sure we set the IDs on our edges from the 'id' attribute.
        // While this is automatic for vertices, it isn't for edges. We keep the
        // definition of this in the IdentifiedWeightedEdge class for convenience.
        importer.addEdgeAttributeConsumer(IdentifiedWeightedEdge::attributeConsumer);

        // On Android, you would use context.getAssets().open(path) here like in Lab 5.
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(path);
        } catch (IOException e) {
            e.printStackTrace();
        }



        //InputStream inputStream = App.class.getClassLoader().getResourceAsStream(path);
        Reader reader = new InputStreamReader(inputStream);

        // And now we just import it!
        importer.importGraph(g, reader);

        return g;
    }

}