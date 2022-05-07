package com.example.zooseekercse110team7.planner;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


/**
 * The purpose of this class is to basically translate function into SQL to query (search) the
 * database. In this class you CAN MODIFY as needed, but make sure to add comments to explain the
 * SQL you've written.
 * */

@Dao
public interface NodeDao {
    /**
     * Inserts item to database
     * */
    @Insert
    long insert(NodeItem nodeItem);
    //inserts a list of items
    @Insert
    List<Long> insertAll(List<NodeItem> nodeItems);

    /**
     * Gets all the Node Items based on String IDs
     * */
    @Query("SELECT * FROM `node_items`")
    List<NodeItem> getAll();
    @Query("SELECT * FROM `node_items` WHERE onPlanner = 0")
    LiveData<List<NodeItem>> getAllLive();
    @Query("SELECT * FROM `node_items` WHERE `id`=:id")
    NodeItem get(String id);

    /**
     * The implementation of the method will update its parameters in the database if they already
     * exists (checked by primary keys). If they don't already exists, this option will not change
     * the database
     * */
    @Update
    int update(NodeItem nodeItem);

    /**
     * The implementation of the method will delete its parameters from the database
     * */
    @Delete
    int delete(NodeItem nodeItem);
    @Query("DELETE FROM `node_items`")
    int deleteAll();
}
