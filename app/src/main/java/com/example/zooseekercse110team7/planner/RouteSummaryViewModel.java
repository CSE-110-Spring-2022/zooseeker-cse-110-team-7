package com.example.zooseekercse110team7.planner;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

public class RouteSummaryViewModel extends AndroidViewModel {
    private List<RouteItem> routeItemList;

    public RouteSummaryViewModel(@NonNull Application application){
        super(application);
    }
}
