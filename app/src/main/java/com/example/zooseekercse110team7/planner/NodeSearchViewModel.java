package com.example.zooseekercse110team7.planner;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NodeSearchViewModel extends AndroidViewModel{
    private List<NodeItem> nodeItems;
    private final NodeDao nodeDao;

    public NodeSearchViewModel(@NonNull Application application){
        super(application);
        Context context = getApplication().getApplicationContext();
        NodeDatabase db = NodeDatabase.getSingleton(context);
        nodeDao = db.nodeDao();
    }

    public List<NodeItem> getAllNodeItems(){
        if(nodeItems == null){
            nodeItems = nodeDao.getAll();
        }

        return nodeItems;
    }

    public void addItemToPlanner(NodeItem nodeItem){
        nodeItem.onPlanner = true;
        nodeDao.update(nodeItem);
    }

    public void removeItemFromPlanner(NodeItem nodeItem){
        nodeItem.onPlanner = false;
        nodeDao.update(nodeItem);
    }
}
