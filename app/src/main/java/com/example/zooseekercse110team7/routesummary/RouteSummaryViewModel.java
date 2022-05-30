package com.example.zooseekercse110team7.routesummary;


import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.zooseekercse110team7.planner.NodeDao;
import com.example.zooseekercse110team7.planner.NodeDatabase;
import com.example.zooseekercse110team7.planner.NodeItem;
import com.example.zooseekercse110team7.planner.ReadOnlyNodeDao;

import org.w3c.dom.Node;

import java.util.List;
//DEPRECIATED
public class RouteSummaryViewModel extends AndroidViewModel {
    private final ReadOnlyNodeDao nodeDao;

    public RouteSummaryViewModel(@NonNull Application application){
        super(application);
        Context context = getApplication().getApplicationContext();
        NodeDatabase db = NodeDatabase.getSingleton(context);
        nodeDao = db.nodeDao();
    }
    public LiveData<List<NodeItem>> getLiveNodeItems(){

        return nodeDao.getAllLive();
    }

}
