package com.example.zooseekercse110team7.planner;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;


/**
 * String   id          - unique id
 * String   name        - name of the exhibit/intersection/etc.
 * String   kind        - category the node belongs to
 * String   tags        - string of comma separated tags
 * bool     completed   - flag that determines if the user completed item
 * bool     onPlanner   - flag that determines if item was added to the planner by the user
 * */
@Entity(tableName = "node_items")
public class NodeItem {
    @PrimaryKey (autoGenerate = false) @NonNull
    public String id;
    @NonNull
    public String name, kind;

    @TypeConverters(StringListToGsonConverter.class)
    public List<String> tags;

    /**
     * This subclass parses an array of strings into a comma separated string and parses comma
     * separated strings into a list
     * */
    public static class StringListToGsonConverter{
        @TypeConverter
        public static List<String> restoreList(String tagListString){
            Log.d("RESTORE", tagListString);
            if(tagListString.equals("\"NULL\"")){
                return Collections.emptyList();
            }
            return new Gson().fromJson(tagListString, new TypeToken<List<String>>(){}.getType());
        }
        @TypeConverter
        public static String saveList(List<String> tags) {
            if(tags.isEmpty()){ return new Gson().toJson("NULL"); }
            return new Gson().toJson(tags);
        }
    }

    /**
     * Note: `tags` is a String and NOT a list -- use pattern matching using LIKE
     *        https://www.sqlitetutorial.net/sqlite-like/
     * */


    public boolean completed, onPlanner;//booleans to check if it's completed or on the planner

    //constructor
    public NodeItem(String id, String name, String kind, List<String> tags){
        this.id = id;
        this.name = name;
        this.kind = kind;
        this.tags = tags;
        this.completed = false; // by default it's an uncompleted task
        this.onPlanner = false; // by default it's not added to the planner
    }

    /**
     * Converts any NodeItem object into a string
     * */
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

    /**
     * Parses the JSON file
     * */
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
