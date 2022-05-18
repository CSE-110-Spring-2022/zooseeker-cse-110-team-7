package com.example.zooseekercse110team7.planner;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * The name of the class may be a tiny bit confusing, but the purpose of this class is to be the
 * MODEL of the MVC pattern
 *
 * It extends to the (Android) MVC framework
 * https://developer.android.com/topic/libraries/architecture/viewmodel
 * */
public class NodeViewModel extends AndroidViewModel {
    //`LiveData` is just a fancy Observer
    //https://developer.android.com/topic/libraries/architecture/livedata
    private List<NodeItem> nodeItems;
    private final NodeDao nodeDao;

    //constructor
    public NodeViewModel(@NonNull Application application){
        super(application);
        Context context = getApplication().getApplicationContext();
        NodeDatabase db = NodeDatabase.getSingleton(context);
        nodeDao = db.nodeDao();
    }

    public LiveData<List<NodeItem>> getLiveNodeItems(){
        return nodeDao.getAllLive();
    }

    private void loadAllNodeItems(){ nodeItems = nodeDao.getAll(); }

    public void deleteItem(NodeItem nodeItem){
        nodeItem.onPlanner = false;
        nodeDao.update(nodeItem);
    }

    public void clearPlanner(){
        nodeDao.clearPlanner();
    }
}
