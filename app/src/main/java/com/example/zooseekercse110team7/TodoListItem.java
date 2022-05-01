package com.example.zooseekercse110team7;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;


@Entity (tableName = "todo_list_items")
public class TodoListItem {
    @PrimaryKey(autoGenerate = true)
    public long id;
    @NonNull
    public String text; public boolean completed; public int order;

    TodoListItem(String text, boolean completed, int order){
        this.text = text;
        this.completed = completed;
        this.order = order;

    }

    @Override
    public String toString() {
        return "TodoListItem{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", completed=" + completed +
                ", order=" + order +
                '}';
    }

    public static List<TodoListItem> loadJSON(Context context, String path){
        //boolean to check if file exists already

        //TODO: Actually create file
        File file = new File("output.json");
        if(!file.exists()){
            //call NodeInfoJsonParse.WriteJson(Context)
            NodeInfoJsonParse n = new NodeInfoJsonParse();
            n.WriteTodoJSON(context);
        }

        try{
            InputStream input = context.getAssets().open(path);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<TodoListItem>>(){}.getType();
            return gson.fromJson(reader,type);
        }catch (IOException e){
            e.printStackTrace();
            return Collections.emptyList();
        }

    }
}
