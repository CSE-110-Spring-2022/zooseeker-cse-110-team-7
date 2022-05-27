package com.example.zooseekercse110team7;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
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

public class SearchActivity extends AppCompatActivity {
    // Exposed for testing purposes later
    public RecyclerView recyclerView;
    public RecyclerView selectedRecyclerView;

    private static final String TAG = "SearchActivity";
    private NodeSearchViewModel viewModel;
    private NodeSearchViewModel selectedViewModel;
    private List<NodeItem> nodeItems = new ArrayList<>();
    private List<NodeItem> selectedNodeItems = new ArrayList<>();
    private NodeSearchViewAdapter nodeViewAdapter;
    private NodeSearchViewAdapter selectedNodeViewAdapter;

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
                nodeViewAdapter.setItems(filter(newText));
                recyclerView.invalidate();
                return false;
            }
        });
    }

    public void onSelectionChange(Boolean added) {
        selectedNodeViewAdapter.setItems(selectedViewModel.getAllSelectedNodeItems());
        selectedRecyclerView.invalidate();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        viewModel = new ViewModelProvider(this).get(NodeSearchViewModel.class);
        selectedViewModel = new ViewModelProvider(this).get(NodeSearchViewModel.class);
        nodeViewAdapter = new NodeSearchViewAdapter(viewModel, this::onSelectionChange);
        selectedNodeViewAdapter = new NodeSearchViewAdapter(selectedViewModel);

        nodeItems = viewModel.getAllFilteredNodeItems("");
        selectedNodeItems = selectedViewModel.getAllSelectedNodeItems();
        if(GlobalDebug.DEBUG){
            for(NodeItem item: nodeItems){
                Log.d("Search", item.toString());
            }
        }

        nodeViewAdapter.setItems(new ArrayList<>(nodeItems));
        selectedNodeViewAdapter.setItems(new ArrayList<>(nodeItems));

        recyclerView = findViewById(R.id.search_node_viewer);//gets the recycler view from `activity_search.xml`
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(nodeViewAdapter);

        selectedRecyclerView = findViewById(R.id.selected_search_node_viewer);
        selectedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectedRecyclerView.setAdapter(selectedNodeViewAdapter);

        setSearchViewListener();

    }

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
