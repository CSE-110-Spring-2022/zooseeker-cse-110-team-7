package com.example.zooseekercse110team7;

import static java.util.Collections.emptyList;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;



public class NodeInfoItem {

    private List<NodeInfoItem> itemsList = new ArrayList<NodeInfoItem>();
    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("kind")
    public String kind;

    @SerializedName("tags")
    public String[] tags;

    public NodeInfoItem(){}

    public NodeInfoItem(String id, String name, String kind, String[] tags){
        this.id = id;
        this.name = name;
        this.kind = kind;
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "NodeInfoJsonParse{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", kind='" + kind + '\'' +
                ", tags=" + Arrays.toString(tags) +
                '}';
    }

    public List<NodeInfoItem> loadJSON(Context context){
        String path = "sample_node_info.json";
        try{
            InputStream input = context.getAssets().open(path);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<NodeInfoItem>>(){}.getType();
            return gson.fromJson(reader,type);
        }catch (IOException e){
            e.printStackTrace();
            return emptyList();
        }

    }

//    public void WriteTodoJSON(Context context){
//        Log.d("Write", "Start----");
//        if(itemsList.isEmpty()){ itemsList = this.loadJSON(context); }
//
//        //TODO:Write a JSON
////        for(NodeInfoJsonParse item: Items){
////            Log.d("PRINTING ITEMS:",item.toString());
////        }
//        String path = "output.json";
//        List<String> names = this.getNames();
//        try {
//            Log.d("Write", "Start");
//            Gson gson = new Gson();
//            Log.d("Write", context.getAssets().toString());
//            Writer writer = Files.newBufferedWriter(Paths.get((context.getAssets()+path)));
//            int i=1;
//            for(String name: names){
//                Log.d("Write", "Write");
//                gson.toJson(new TodoListItem(name,false, i++), writer);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

    public List<String> getNames(){
        if(itemsList.isEmpty()){
            Log.e("NodeInfoJsonParse.java, getNames", "file has not been parsed");
            return Collections.emptyList();
        }

        List<String> nameList = new ArrayList<>();

        for(NodeInfoItem item: itemsList){
            nameList.add(item.name);
        }

        return nameList;
    }
}
