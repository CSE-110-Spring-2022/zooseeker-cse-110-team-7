package com.example.zooseekercse110team7.planner;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.w3c.dom.Node;

import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {NodeItem.class}, version = 1)
public abstract class NodeDatabase extends RoomDatabase {
    private static NodeDatabase singleton = null;

    public abstract NodeDao nodeDao();

    public synchronized static NodeDatabase getSingleton(Context context){
        if(singleton == null){
            singleton = NodeDatabase.makeDatabase(context);
        }

        return singleton;
    }

    public static NodeDatabase makeDatabase(Context context){
        return Room.databaseBuilder(context, NodeDatabase.class, "planner_app.db")
                .allowMainThreadQueries()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(() -> {
                            List<NodeItem> todos = NodeItem.
                                    loadJSON(context, "sample_node_info.json");
                            getSingleton(context).nodeDao().insertAll(todos);
                        });
                    }
                }).build();
    }


    @VisibleForTesting
    public static void injectTestDatabase(NodeDatabase testDatabase){
        if(singleton != null){
            singleton.close();
        }
        singleton = testDatabase;
    }
}