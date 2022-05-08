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
import android.widget.TextView;

import com.example.zooseekercse110team7.planner.NodeItem;
import com.example.zooseekercse110team7.planner.NodeSearchViewAdapter;
import com.example.zooseekercse110team7.planner.NodeSearchViewModel;
import com.example.zooseekercse110team7.planner.NodeViewAdapter;
import com.example.zooseekercse110team7.planner.NodeViewModel;

import java.util.List;

public class PlannerActivity extends AppCompatActivity {
    // Exposed for testing purposes later
    public RecyclerView recyclerView;

    private NodeViewModel viewModel;
    private TextView numberItemsTextView;
    private void setNumberItemsTextView() {
        final Observer<List<NodeItem>> nameObserver = new Observer<List<NodeItem>>() {
            @Override
            public void onChanged(@Nullable final List<NodeItem> newName) {
                // Update the UI, in this case, a TextView.
                String number = "POI: " + String.valueOf(newName.size());
                numberItemsTextView.setText(number);
            }
        };
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        viewModel.getLiveNodeItems().observe(this, nameObserver);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        //Observer item count
        numberItemsTextView = findViewById(R.id.number_items_tv);
        viewModel = new ViewModelProvider(this).get(NodeViewModel.class);//



        //Added item count observer
        setNumberItemsTextView();

        NodeViewAdapter nodeViewer = new NodeViewAdapter();
        viewModel.getLiveNodeItems().observe(this, nodeViewer::setItems);//

        recyclerView = findViewById(R.id.node_viewer);//gets the recycler view from `activity_planer.xml`
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(nodeViewer);

        nodeViewer.setOnDeleteButtonClicked(viewModel::deleteItem);
//        nodeViewer.setItems(NodeItem.loadJSON(this, "sample_node_info.json"));
    }

    public void onMapClicked(View view){
        Intent intent = new Intent(PlannerActivity.this, MapsActivity.class);
        startActivity(intent);
    }

    public void onSearchClicked(View view){
        Intent intent = new Intent(PlannerActivity.this, SearchActivity.class);
        startActivity(intent);
    }
}