package com.example.zooseekercse110team7.planner;


import android.content.Context;
import android.util.Log;

import java.util.List;


/**
 * The purpose of this class is to allow other classes to request particular functions or
 * information from the database. The naming of this class is intentional as a reminder that
 * updating the database can be tracked to its initial purpose. Moreover, it's to try and prevent
 * other classes/developers from updating the database when they shouldn't.
 * */
public class NodeDaoRequest {
    //  Singleton Setup
    // ---------------------------------------START---------------------------------------------- //
    public static NodeDaoRequest instance = new NodeDaoRequest();
    private NodeDaoRequest(){}
    public static NodeDaoRequest getInstance(){ return instance; }
    // ----------------------------------------END----------------------------------------------- //

    private NodeDao nodeDao;

    /**
     * Sets the context to be able to get a reference to the `NodeDao`.
     *
     * @param aContext the context in which to get a NodeDao from
     *
     * @return this instance of the object which allows for Chain of Command
     * */
    public NodeDaoRequest setNodeDao(Context aContext){
        nodeDao = NodeDatabase.getSingleton(aContext).nodeDao();
        return this;
    }
    /* SAME METHOD, DIFFERENT NAME */
    public NodeDaoRequest setContext(Context aContext){
        nodeDao = NodeDatabase.getSingleton(aContext).nodeDao();
        return this;
    }


    /**
     * Upon request, the database is updated to change the boolean of value of node's `onPlanner`
     * field to false.
     *
     * @param id the ID of the node to update
     *
     * @return a boolean of success or failure
     * */
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


    /**
     * Returns an item in the database -- does not do any checks.
     *
     * @param id the ID of the NodeItem to find
     *
     * @return the found NodeItem in the database
     * */
    public NodeItem RequestItem(String id){
        return nodeDao.get(id);
    }

    /**
     * Find the NodeItem with "gate" `kind` attribute of a NodeItem. (kind=="gate")
     *
     * @return the ID of the NodeItem that matches "gate" `kind`
     * */
    public String RequestGateId(){
        String gateString = "%gate%";
        try{
            return nodeDao.getGate(gateString).id;
        }catch (NullPointerException e){
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Returns the list of children associated with a parent ID.
     *
     * @param parentId ID of the parent
     *
     * @return a list of NodeItems associated with the parent ID
     * */
    public List<NodeItem> RequestChildrenOf(String parentId){
        return nodeDao.getChildren(parentId);
    }

    /**
     * Determines if an ID is a parent ID.
     *
     * @param anId a NodeItem ID in the database
     *
     * @return true or false -- a boolean about the resulting query
     * */
    public boolean isParent(String anId){
        try{
            NodeItem temp = nodeDao.getParent(anId);
            return true;//if not null
        }catch (NullPointerException e){
            return false;
        }
    }

    /**
     * Finds a list of NodeItems that are on the Planner.
     *
     * @return a list of NodeItems on the Planner
     * */
    public List<NodeItem> RequestPlannedItems(){
        return nodeDao.getByOnPlanner(true);
    }

    /**
     * Finds the name associated with a NodeItem through an ID.
     *
     * @param anId the ID of a NodeItem in the database
     *
     * @return the name of the of item, empty if nonexistent item
     * */
    public String RequestName(String anId){
        try{
            return nodeDao.get(anId).name;
        }catch (NullPointerException e){
            e.printStackTrace();
            return anId; // return id if cannot find node item
        }
    }
}
