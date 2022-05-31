package com.example.zooseekercse110team7;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.zooseekercse110team7.planner.FilterDialogViewAdapter;
import com.example.zooseekercse110team7.planner.NodeItem;
import com.example.zooseekercse110team7.planner.NodeSearchViewAdapter;
import com.example.zooseekercse110team7.planner.NodeSearchViewModel;
import com.example.zooseekercse110team7.planner.NodeViewAdapter;
import com.example.zooseekercse110team7.planner.NodeViewModel;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

//here
public class SearchActivity extends AppCompatActivity {
    // Exposed for testing purposes later
    public RecyclerView recyclerView;
    // recycler view for selected nodes
    public RecyclerView selectedRecyclerView;

    private static final String TAG = "SearchActivity";
    // view model for all nodes
    private NodeSearchViewModel viewModel;
    // view model for selected nodes
    private NodeSearchViewModel selectedViewModel;
    // all nodes list
    private List<NodeItem> nodeItems = new ArrayList<>();
    // all nodes for the selected ones
    private List<NodeItem> selectedNodeItems = new ArrayList<>();
    // all nodes nodesearchview adapter
    private NodeSearchViewAdapter nodeViewAdapter;
    // selected nodes nodesearchview adapater
    private NodeSearchViewAdapter selectedNodeViewAdapter;

    // filters the nodes by what customer wants
    private List<NodeItem> filter(String filterString) {
        return viewModel.getAllFilteredNodeItems(filterString);
    }

    private void setSearchViewListener() {
        SearchView searchView = (SearchView) findViewById(R.id.search);

        // perform set on query text listener event
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
            // do something on text submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "new text = " + newText);
                // sets selected items and shows on recyclerview
                nodeViewAdapter.setItems(filter(newText));
                recyclerView.invalidate();
                return false;
            }
        });
    }

    /**
     * Sets which items are selected in search to show the complete list
     *
     * */
    public void onSelectionChange(Boolean added) {
        selectedNodeViewAdapter.setItems(selectedViewModel.getAllSelectedNodeItems());
        selectedRecyclerView.invalidate();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // creates view model for all nodes
        viewModel = new ViewModelProvider(this).get(NodeSearchViewModel.class);
        //creates view model for selected nodes
        selectedViewModel = new ViewModelProvider(this).get(NodeSearchViewModel.class);
        // creates node view adapter for all nodes
        nodeViewAdapter = new NodeSearchViewAdapter(viewModel, this::onSelectionChange);
        // creates node view adapter for selected nodes
        selectedNodeViewAdapter = new NodeSearchViewAdapter(selectedViewModel);

        // declares all nodeItems
        nodeItems = viewModel.getAllFilteredNodeItems("");
        // declares all selectedNodeItems
        selectedNodeItems = selectedViewModel.getAllSelectedNodeItems();
        if(GlobalDebug.DEBUG){
            for(NodeItem item: nodeItems){
                Log.d("Search", item.toString());
            }
        }

        // sets all nodes
        nodeViewAdapter.setItems(new ArrayList<>(nodeItems));
        // selectedNodeViewAdapter.setItems(new ArrayList<>(nodeItems));

        // recycler view creation for all the nodes
        recyclerView = findViewById(R.id.search_node_viewer);//gets the recycler view from `activity_search.xml`
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(nodeViewAdapter);

        // recycler view creation for the selected nodes
        selectedRecyclerView = findViewById(R.id.selected_search_node_viewer);
        selectedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectedRecyclerView.setAdapter(selectedNodeViewAdapter);

        setSearchViewListener();

    }

    /**
     * Clears everything selected in search with one button
     *
     * */
    public void onResetClick(View view) {
        viewModel.removeAllItemsFromPlanner();
        nodeViewAdapter.setItems(filter(""));
        recyclerView.invalidate();
        selectedNodeViewAdapter.setItems(selectedViewModel.getAllSelectedNodeItems());
        selectedRecyclerView.invalidate();
    }

    /**
     * When the filter image is clicked, a dialog appears which has options which the user can
     * filter by.
     *
     * @param view layout of the current UI
     * */
    public void onFilterClick(View view){
        if(viewModel == null || nodeViewAdapter == null){
            Log.d("Search", "| Filter Cannot Load, Model or Adapter is NULL");
            return;
        }

        //open/create filter dialog
        Log.d("Search", "| Creating Filter Dialog");
        Dialog filterDialog = new Dialog(this);
        filterDialog.setContentView(R.layout.filter_dialog);
        filterDialog.show();
        Log.d("Search", "| Dialog Shown");

        Log.d("Search", "| Creating Adapter For Dialog");
        FilterDialogViewAdapter dialogViewAdapter = new FilterDialogViewAdapter(viewModel);
        Log.d("Search", "| Setting Items In Dialog");
        dialogViewAdapter.setItems(viewModel.getFilters());

        Log.d("Search", "| Creating & Updating Recycler");
        RecyclerView recyclerView = filterDialog.findViewById(R.id.filter_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(dialogViewAdapter);

        Log.d("Search", "| Update Listener Set");
        dialogViewAdapter.setOnUpdateFilters(viewModel::updateFilterItem);

        Button closeBtn = filterDialog.findViewById(R.id.close_filter_dialog_btn);
        closeBtn.setOnClickListener((aView)->{
            Log.d("Search", "Closing Dialog");
            filterDialog.dismiss();

            // update view
            Log.d("Search", "| Updating View With Filtered Nodes");
            nodeViewAdapter.setItems(viewModel.getAllFilteredNodeItems(""));
        });
        Log.d("Search", "| Close Dialog Button Set");
        Log.d("Search", "| End of Function");

    }

}
