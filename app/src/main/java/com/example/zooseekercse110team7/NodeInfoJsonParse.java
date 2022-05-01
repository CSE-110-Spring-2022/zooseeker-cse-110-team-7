package com.example.zooseekercse110team7;

import android.content.Context;
import android.util.Log;

import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


//TODO (More of a Notice): It's an interface between the JSON file & connecting the
// data to the different parts of the app. This eliminates the need to heavily modify the todolist
// because it will create a file that is like `demo_todos.json` but the `text` is the `names` of
// the `sample_node_info.json`

public class NodeInfoJsonParse {
//    @SerializedName("id")
    //TODO: give it and actual id number
    //see piazza: https://piazza.com/class/l186r5pbwg2q4?cid=460
    @PrimaryKey(autoGenerate = true)
    public String idString;

    @SerializedName("name")
    public String name;

    @SerializedName("kind")
    public String kind;

    @SerializedName("tags")
    public String[] tags;

    public NodeInfoJsonParse(){

    }

    @Override
    public String toString() {
        return "NodeInfoJsonParse{" +
                "idString='" + idString + '\'' +
                ", name='" + name + '\'' +
                ", kind='" + kind + '\'' +
                ", tags=" + Arrays.toString(tags) +
                '}';
    }

    public List<NodeInfoJsonParse> loadJSON(Context context){
        String path = "sample_node_info.json";
        try{
            InputStream input = context.getAssets().open(path);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<NodeInfoJsonParse>>(){}.getType();
            return gson.fromJson(reader,type);
        }catch (IOException e){
            e.printStackTrace();
            return Collections.emptyList();
        }

    }

    public void WriteTodoJSON(Context context){
        List<NodeInfoJsonParse> Items = this.loadJSON(context);
        //TODO:Write a JSON
        //Note: `sample_node_info.json` has IDs changed to numbers
        for(NodeInfoJsonParse item: Items){
            Log.d("PRINTING ITEMS:",item.toString());
        }
    }
}
