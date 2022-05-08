package com.example.zooseekercse110team7.planner;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NodeSearchViewModel extends AndroidViewModel{
    private LiveData<List<NodeItem>> nodeItems;
    private final NodeDao nodeDao;

    public NodeSearchViewModel(@NonNull Application application){
        super(application);
        Context context = getApplication().getApplicationContext();
        NodeDatabase db = NodeDatabase.getSingleton(context);
        nodeDao = db.nodeDao();
    }

    public LiveData<List<NodeItem>> getLiveNodeItems(){
        if(nodeItems == null){
            loadUsers();
        }

        return nodeItems;
    }

    private void loadUsers(){ nodeItems = nodeDao.getAllLiveForSearch(); }

    public void addItem(NodeItem nodeItem){
        nodeItem.onPlanner = true;
        nodeDao.update(nodeItem);
    }

    //TODO: Add Node Item
//    public void AddItem(/*params*/){
//        // add item to database
//    }
}
