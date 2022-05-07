package com.example.zooseekercse110team7;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.zooseekercse110team7.planner.NodeItem;
import com.example.zooseekercse110team7.planner.NodeSearchViewAdapter;
import com.example.zooseekercse110team7.planner.NodeSearchViewModel;
import com.example.zooseekercse110team7.planner.NodeViewAdapter;
import com.example.zooseekercse110team7.planner.NodeViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {
    // Exposed for testing purposes later
    public RecyclerView recyclerView;

    private static final String TAG = "SearchActivity";
    private NodeSearchViewModel viewModel;
    private List<NodeItem> nodeItems = new ArrayList<>();
    private NodeSearchViewAdapter nodeViewAdapter = new NodeSearchViewAdapter();

    private List<NodeItem> filter(String filterString) {
        filterString = filterString.toLowerCase();
        List<NodeItem> finalList = new ArrayList<NodeItem>();
        Log.d(TAG, "nodeitemsize: " + nodeItems.size());
        if (filterString.isEmpty()) {
            Log.d(TAG, "returning item: " + nodeItems.size());
            finalList.addAll(nodeItems);
            return finalList;
        }
        for (NodeItem nodeItem: nodeItems) {
            Log.d(TAG, "comparing item: " + nodeItem.name);
            if (nodeItem.name.toLowerCase().startsWith(filterString)) {
                Log.d(TAG, "item matched: " + nodeItem.name);
                finalList.add(nodeItem);
            }
        }
        Log.d(TAG, "returning finalList: " + finalList.size());
        return finalList;
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
                return false;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        viewModel = new ViewModelProvider(this).get(NodeSearchViewModel.class);//

        List<NodeItem> allItems = viewModel.getAllNodeItems();
        for (NodeItem item : allItems) {
            if (item.kind.equals("exhibit")) {
                nodeItems.add(item);
            }
        }
        nodeViewAdapter.setItems(new ArrayList<>(nodeItems));

        recyclerView = findViewById(R.id.search_node_viewer);//gets the recycler view from `activity_search.xml`
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(nodeViewAdapter);

        setSearchViewListener();

        nodeViewAdapter.setOnAddButtonClicked(viewModel::addItem);
    }

}
