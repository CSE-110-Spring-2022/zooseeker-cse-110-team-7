package com.example.zooseekercse110team7.planner;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

@Entity(tableName = "node_items")
public class NodeItem {
    @PrimaryKey (autoGenerate = false) @NonNull
    public String id;
    @NonNull
    public String name, kind, tags;
    /**
     * Note: `tags` is a String and NOT a list -- use pattern matching using LIKE
     *        https://www.sqlitetutorial.net/sqlite-like/
     * */

    public boolean completed, onPlanner;

    NodeItem(String id, String name, String kind, String tags){
        this.id = id;
        this.name = name;
        this.kind = kind;
        this.tags = tags;
        this.completed = false; // by default it's an uncompleted task
        this.onPlanner = false; // by default it's not added to the planner
    }

    @Override
    public String toString() {
        return "NodeItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", kind='" + kind + '\'' +
                ", tags='" + tags + '\'' +
                ", completed=" + completed +
                ", onPlanner=" + onPlanner +
                '}';
    }

    public static List<NodeItem> loadJSON(Context context, String path){
        Log.d("NodeItem_Load_JSON", path);
        try{
            InputStream input = context.getAssets().open(path);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<NodeItem>>(){}.getType();
            return gson.fromJson(reader,type);
        }catch (IOException e){
            e.printStackTrace();
            return Collections.emptyList();
        }

    }
}
