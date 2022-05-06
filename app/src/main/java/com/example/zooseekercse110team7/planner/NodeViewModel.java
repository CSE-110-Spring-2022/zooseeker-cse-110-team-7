package com.example.zooseekercse110team7.planner;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NodeViewModel extends AndroidViewModel {
    private LiveData<List<NodeItem>> nodeItems;
    private final NodeDao nodeDao;

    public NodeViewModel(@NonNull Application application){
        super(application);
        Context context = getApplication().getApplicationContext();
        NodeDatabase db = NodeDatabase.getSingleton(context);
        nodeDao = db.nodeDao();
    }

    public LiveData<List<NodeItem>> getNodeItems(){
        if(nodeItems == null){
            loadUsers();
        }

        return nodeItems;
    }

    private void loadUsers(){ nodeItems = nodeDao.getAllLive(); }

    //TODO: Delete Node Item -- We want to remove from planner, not database
//    public void deleteItem(NodeItem nodeItem){ nodeDao.delete(nodeItem); }

    //TODO: Add Node Item
//    public void AddItem(/*params*/){
//        // add item to database
//    }
}
