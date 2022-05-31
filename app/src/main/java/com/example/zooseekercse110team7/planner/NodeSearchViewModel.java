package com.example.zooseekercse110team7.planner;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.zooseekercse110team7.GlobalDebug;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


/**
 * The name of the class may be a tiny bit confusing, but the purpose of this class is to be the
 * MODEL of the MVC pattern
 *
 * It extends to the (Android) MVC framework
 * https://developer.android.com/topic/libraries/architecture/viewmodel
 * */
public class NodeSearchViewModel extends AndroidViewModel{
    private final NodeDao nodeDao;

    // Note: reason for static is because we want to make sure they don't have to constantly set
    // the filters
    private static HashSet<FilterItem> filterItems = new HashSet<>();

    //constructor
    public NodeSearchViewModel(@NonNull Application application){
        super(application);
        Context context = getApplication().getApplicationContext();
        NodeDatabase db = NodeDatabase.getSingleton(context);
        nodeDao = db.nodeDao();
        getFilters();//we call this here to make sure there is something visible when the user
                     // goes into the search activity
    }

    public List<NodeItem> getAllNodeItems(){
        return nodeDao.getAll();
    }

    /**
     * This function retrieves all the values from the `kind` fields from the database. For clarity,
     * if there are a total of 3 `kinds` (i.e categories) and 10 `NodeItem`s, then it will initially
     * get 10 values even if they repeat. It's not until the end of the function where repeated
     * items are removed from the list. However, it should be noted that `FilterItem` objects are
     * created and stored in a HashSet to ensure there are no duplicates and intended behavior.
     * If the HashSet is empty at the start of this method, then it will always try to find `kind`
     * fields.
     * Once the HashSet has at least 1 item it will NEVER try and update itself even when the
     * database is updated with more items.
     *
     * @return a List of `FilterItem`s with no duplicates
     * */
    public List<FilterItem> getFilters(){
        //if the set is NOT empty, don't get anything from database
        if(!filterItems.isEmpty()){
            Log.d("NodeSearchViewModel[Filters]", "| Already Retrieved Filters...Returning");
            return new ArrayList<FilterItem>(filterItems); }


        List<FilterItem> filterItems =
                new ArrayList<>(); // result - holds a list of FilterItem objects
        Log.d("NodeSearchViewModel[Filters]", "Retrieving String of All `kind` Fields");
        List<String> kindStrings =
                new ArrayList<>(new HashSet<>(nodeDao.getAllKindNames())); // value of all `kind`
                                                                           // fields in database

        /* CREATE `FilterItem` & HAVE `exhibit` BE ON THE FILTER BY DEFAULT*/
        for(String item: kindStrings){
            Log.d("NodeSearchViewModel[Filters]", "|-> String Name: " + item);
            if(item.equals("exhibit")){
                Log.d("NodeSearchViewModel[Filters]", "||--> exhibit on filter");
                filterItems.add(new FilterItem(item, true));
            }else {
                filterItems.add(new FilterItem(item, false));
            }
        }

        Log.d("NodeSearchViewModel[Filters]", "| Reassigning HashSet");
        this.filterItems = new HashSet<FilterItem>(filterItems);

        Log.d("NodeSearchViewModel[Filters]", "| Success Returning Name Of Filters");
        return filterItems;
    }
    //here

    public List<NodeItem> getAllSelectedNodeItems() {
        return nodeDao.getByOnPlanner(true);
    }

    public List<NodeItem> getAllFilteredNodeItems(String filter){
        String queryString;
        if (filter.isEmpty()) {
            queryString = "%";
        }
        else {
            queryString = "%" + filter.toLowerCase() + "%";
        }
        List<Boolean> onPlannerBools = new ArrayList<>();
        onPlannerBools.add(true);
        onPlannerBools.add(false);
        /**
         * the following lines are now depreciated with the feature to have the user filter items
         * themselves.
         * These lines have now been replace with the `getListOfStringFilters` method.
         * */
//        List<String> kinds = new ArrayList<>();
//        kinds.add("exhibit");
//        kinds.add("intersection");
//        kinds.add("undefined");

        if(GlobalDebug.DEBUG){
            Log.d("SearchViewModel", "GETTING DATABASE ITEMS");
            List<NodeItem> nodes = nodeDao.getAll();
            for(NodeItem node: nodes){
                Log.d("SearchViewModel", node.toString());
            }
        }
        return nodeDao.getByFilter(onPlannerBools, getListOfStringFilters()/*kinds*/, queryString);
    }

    public void addItemToPlanner(NodeItem nodeItem){
        nodeItem.onPlanner = true;
        nodeDao.update(nodeItem);
    }

    public void removeItemFromPlanner(NodeItem nodeItem){
        nodeItem.onPlanner = false;
        nodeDao.update(nodeItem);
    }

    public void removeAllItemsFromPlanner() {
        nodeDao.clearPlanner();
    }

    /**
     * This method updates a filter item object within the HashSet which determines if an item will
     * be filtered or not.
     *
     * @param filterItem updated `FilterItem` field(s)
     * */
    public void updateFilterItem(FilterItem filterItem){
        Log.d("NodeSearchViewModel[Filters]", "| Updating Filter Object");

        if(GlobalDebug.DEBUG) {
            for (FilterItem anItem : filterItems) {
                Log.d("Update", "|-> Item: " + filterItems.toString());
            }
        }

        if(filterItems.isEmpty()){ Log.d("NodeSearchViewModel[Filters]", "|-> Is Empty!");}

        filterItems.stream().filter(item->item.getName().equals(filterItem.getName()))
                .findFirst()
                .ifPresent(p -> {
                    p.setOnFilter(filterItem.isOnFilter());
                    Log.d("NodeSearchViewModel[Filters]", "| Item Found!");
                });

    }

    /**
     * Returns a list of filters in terms of their name (i.e string). Moreover, only those that are
     * `onFilter` will be within the list.
     *
     * @return a list `kind` values that the user wants filtered (i.e included in search)
     * */
    private List<String> getListOfStringFilters(){
        List<String> resultList = new ArrayList<>(); // will contain final result

        /* ADDS ITEMS TO `resultList` */
        for(FilterItem anItem: filterItems){
            if(!anItem.isOnFilter()){ continue; } // skip item if not on filter
            resultList.add(anItem.getName());
        }

        return resultList;
    }
}
