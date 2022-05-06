package com.example.zooseekercse110team7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.zooseekercse110team7.planner.NodeItem;
import com.example.zooseekercse110team7.planner.NodeViewAdapter;
import com.example.zooseekercse110team7.planner.NodeViewModel;

import java.util.List;

public class PlannerActivity extends AppCompatActivity {
    // Exposed for testing purposes later
    public RecyclerView recyclerView;

    private NodeViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        viewModel = new ViewModelProvider(this).get(NodeViewModel.class);//
        NodeViewAdapter nodeViewer = new NodeViewAdapter();
        viewModel.getNodeItems().observe(this, nodeViewer::setPlannerItems);//

        recyclerView = findViewById(R.id.node_viewer);//gets the recycler view from `activity_planer.xml`
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(nodeViewer);

        nodeViewer.setOnDeleteButtonClicked(viewModel::deleteItem);
//        nodeViewer.setPlannerItems(NodeItem.loadJSON(this, "sample_node_info.json"));
    }
}