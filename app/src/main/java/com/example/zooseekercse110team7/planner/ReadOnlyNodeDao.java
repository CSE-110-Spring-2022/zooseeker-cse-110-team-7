package com.example.zooseekercse110team7.planner;

import androidx.lifecycle.LiveData;

import java.util.List;

public interface ReadOnlyNodeDao {
    List<NodeItem> getAll();
    LiveData<List<NodeItem>> getAllLive();
    NodeItem get(String id);
    List<NodeItem> getByFilter(List<Boolean> onPlannerBools,
                               List<String> kinds,
                               String queryString);
    List<NodeItem> getByKind(List<Boolean> onPlannerBools, List<String> kinds);
    List<NodeItem> getByOnPlanner(Boolean onPlannerBools);
    List<String> getAllKindNames();
}
