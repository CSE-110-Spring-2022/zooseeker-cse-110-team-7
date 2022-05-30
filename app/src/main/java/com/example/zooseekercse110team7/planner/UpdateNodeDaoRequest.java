package com.example.zooseekercse110team7.planner;


import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.util.Optional;

public class UpdateNodeDaoRequest {
    public static UpdateNodeDaoRequest instance = new UpdateNodeDaoRequest();
    private UpdateNodeDaoRequest(){}
    public static UpdateNodeDaoRequest getInstance(){ return instance; }

    private NodeDao nodeDao;

    public UpdateNodeDaoRequest setNodeDao(Context aContext){
        nodeDao = NodeDatabase.getSingleton(aContext).nodeDao();
        return this;
    }
    public UpdateNodeDaoRequest setContext(Context aContext){
        nodeDao = NodeDatabase.getSingleton(aContext).nodeDao();
        return this;
    }

    public boolean RequestPlannerSkip(String id){
        if(nodeDao == null){
            Log.d("UpdateNodeDaoRequest", "Node Dao is NULL! -- Request Failed");
            return false;
        }

        if(1 != nodeDao.removeItemOnPlanner(id)){
            Log.d("UpdateNodeDaoRequest", "Did not find Item or Multiple Items Found");
            return false;
        }

        return !nodeDao.get(id).onPlanner;
    }


    //TODO: Depreciate these methods -- in violation of SRP
    public NodeItem RequestItem(String id){
        return nodeDao.get(id);
    }
    public String RequestGateId(){
        String gateString = "%gate%";
        try{
            return nodeDao.getGate(gateString).id;
        }catch (NullPointerException e){
            return "";
        }
    }
}
