package com.example.zooseekercse110team7;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.zooseekercse110team7.map_v2.Path;
import com.example.zooseekercse110team7.planner.NodeItem;
import com.example.zooseekercse110team7.planner.NodeViewAdapter;
import com.example.zooseekercse110team7.planner.NodeViewModel;
import com.example.zooseekercse110team7.routesummary.RouteItem;
import com.example.zooseekercse110team7.routesummary.RouteSummary;
import com.example.zooseekercse110team7.routesummary.RouteSummaryViewAdapter;
import com.example.zooseekercse110team7.routesummary.RouteSummaryViewModel;

import java.util.List;


public class PlannerActivity extends AppCompatActivity {
    // Exposed for testing purposes later
    public RecyclerView recyclerView, routeSummaryView;
    private NodeViewModel viewModel; private RouteSummaryViewModel routeViewModel;


    private TextView numberItemsTextView;

    RouteSummary summary = RouteSummary.getInstance();
    //GraphPathSingleton graph_path = GraphPathSingleton.getInstance();
    Path graph_path = Path.getInstance();

    private void nodeDaoObserver(RouteSummaryViewAdapter adapter) {
        final Observer<List<NodeItem>> nodeObserver = new Observer<List<NodeItem>>() {
            @Override
            public void onChanged(@Nullable final List<NodeItem> newName) {
                Log.d("Planner", "Database Updated!");
                // Update the UI, in this case, a TextView.
                String number = "POI: " + String.valueOf(newName.size());
                numberItemsTextView.setText(number);

                if(GlobalDebug.DEBUG){
                    Log.d("Planner", "Checking Items In Planner After DB Update");
                    List<NodeItem> itemList = viewModel.getNodePlannerItems();
                    for(NodeItem item: itemList){
                        Log.d("Planner", item.toString());
                    }
                }

                summary.updateRouteSummary(viewModel.getNodePlannerItems());
                if(GlobalDebug.DEBUG){
                    List<RouteItem> itemList = summary.getItems();
                    for(RouteItem item: itemList){
                        Log.d("Planner", item.toString());
                    }
                }
                adapter.notifyView();
            }
        };
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        viewModel.getLiveNodeItems().observe(this, nodeObserver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        //Observer item count
        numberItemsTextView = findViewById(R.id.number_items_tv);
        viewModel = new ViewModelProvider(this).get(NodeViewModel.class);//



        //Added item count observer
        //graph_path.setNodeItems(viewModel.getNodePlannerItems());
        //graph_path.loadAssets(getApplicationContext());


        NodeViewAdapter nodeViewer = new NodeViewAdapter();
        viewModel.getLiveNodeItems().observe(this, nodeViewer::setItems);//

        recyclerView = findViewById(R.id.node_viewer);//gets the recycler view from `activity_planer.xml`
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(nodeViewer);

        nodeViewer.setOnDeleteButtonClicked(viewModel::deleteItem);

        /*-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-*/
        //TODO: Update UI

        //routeViewModel = new ViewModelProvider(this).get(RouteSummaryViewModel.class);
        RouteSummaryViewAdapter routeSummaryViewAdapter = new RouteSummaryViewAdapter();
        //routeViewModel.getLiveNodeItems().observe(this, routeSummaryViewAdapter::setItems);


        routeSummaryView = findViewById(R.id.route_summary_viewer);
        routeSummaryView.setLayoutManager(new LinearLayoutManager(this));
        routeSummaryView.setAdapter(routeSummaryViewAdapter);

        nodeDaoObserver(routeSummaryViewAdapter);
    }

    public void onClearAllClicked(View view){
        Log.d("NodeViewModel[Clear Button]", "Clear Button Clicked!");
        if(recyclerView == null || viewModel == null){
            Log.d("NodeViewModel[Clear Button]", "Model and RecyclerView are NULL!\nNot Clearing!");
            return;
        }

        viewModel.clearPlanner();
    }

    public void onMapClicked(View view){
        Intent intent = new Intent(PlannerActivity.this, MapsActivity.class);
        startActivity(intent);
    }

    public void onSearchClicked(View view){
        Intent intent = new Intent(PlannerActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(this).edit();
        e.putString("last_activity", getClass().getSimpleName());
        e.commit();

        super.onResume();
    }
}