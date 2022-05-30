package com.example.zooseekercse110team7.planner;



/**
 * An object that contains information on the filter can be added.
 * */
public class FilterItem {
    private String name;
    private boolean onFilter;
    public FilterItem(String name, boolean onFilter){
        this.name = name;
        this.onFilter = onFilter;
    }

    public String getName(){ return name; }
    public boolean isOnFilter(){ return onFilter; }
    public void setOnFilter(boolean onFilter){ this.onFilter = onFilter; }

    @Override
    public String toString() {
        return "FilterItem{" +
                "name='" + name + '\'' +
                ", onFilter=" + onFilter +
                '}';
    }
}
