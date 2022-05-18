package com.example.zooseekercse110team7.planner;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.w3c.dom.Node;

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
    @Query("SELECT * FROM `node_items` ORDER BY name")
    List<NodeItem> getAll();
    @Query("SELECT * FROM `node_items` WHERE onPlanner = 1")
    LiveData<List<NodeItem>> getAllLive();
    @Query("SELECT * FROM `node_items` WHERE `id`=:id")
    NodeItem get(String id);

    /**
     * Finds items based on conditions
     * @param onPlannerBools list of booleans such that when querying it finds `onPlanner`
     *                       values equal to any value within the list
     * @param kinds list of Strings that filter nodes if `kind` equals to one of the String elements
     *              in the list. Example: {"exhibit", "intersection"} will filter for `kind`s that
     *              are either exhibit or intersection
     * @param queryString String that the user has typed related to the tags
     * */
    @Query("SELECT * FROM `node_items` " +
            "WHERE onPlanner IN (:onPlannerBools) " +
            "AND kind IN (:kinds) " +
            "AND (tags LIKE :queryString OR name LIKE :queryString)")
    List<NodeItem> getByFilter(List<Boolean> onPlannerBools,
                               List<String> kinds,
                               String queryString);//%a_string%
    @Query("SELECT * FROM `node_items` WHERE onPlanner IN (:onPlannerBools) " +
            "AND kind IN (:kinds)")
    List<NodeItem> getByKind(List<Boolean> onPlannerBools, List<String> kinds);
    @Query("SELECT * FROM `node_items` WHERE onPlanner IN (:onPlannerBools)")
    List<NodeItem> getByOnPlanner(Boolean onPlannerBools);

    /**
     * The implementation of this method will set all `NodeItem`s `onPlanner` field to false. This
     * will indicate that there are no items on the planner.
     *
     * @return the number of updated items that had `onPlanner` equal to true/one
     * */
    @Query("UPDATE `node_items` SET onPlanner=0 WHERE onPlanner=1")
    int clearPlanner();

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
