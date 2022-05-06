package com.example.zooseekercse110team7.planner;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zooseekercse110team7.R;
import com.example.zooseekercse110team7.TodoListAdapter;

import java.util.Collections;
import java.util.List;

/**
 * This class updates the Recycler Viewer (UI list). It extends to the RecyclerView
 * Adapter to be able to update it. It holds all the important methods dealing with the
 * implementation of Recycler Viewer. The basic methods for a successful implementation are:
 * - onCreateViewHolder
 * - onBindHolder
 * - getItemCount
 *
 * The ViewHolder is a java class that stores the reference to the card layout views that have to be
 * dynamically modified during the execution of the program by a list of data obtained by the
 * database
 * Note: 'card layout' refers to the how an item is displayed in terms of the UI (i.e name, kind,
 * and delete button)
 * */
public class NodeViewAdapter extends RecyclerView.Adapter <NodeViewAdapter.ViewHolder>{
    private List<NodeItem> plannerItems = Collections.emptyList();

    public void setPlannerItems(List<NodeItem> newPlannerItems){
        plannerItems.clear();
        plannerItems = newPlannerItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.node_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setItem(plannerItems.get(position));
    }

    @Override
    public int getItemCount() {
        return plannerItems.size();
    }

//    @Override
//    public long getItemId(int position){ return plannerItems.get(position).id; }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private NodeItem nodeItem;      // current node item -- useful if helper functions used
        private final TextView nameTextView, kindTextView; // text views for `name` and `kind`
        private final Button deleteBtn; // delete button view

        //constructor
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            nameTextView = itemView.findViewById(R.id.node_name_tv);//note: 'tv' means 'text view'
            kindTextView = itemView.findViewById(R.id.node_kind_tv);
            deleteBtn = itemView.findViewById(R.id.node_delete_btn);
        }

        /**
         * Displays the exhibit the user wants to visit. It also prevents and Null Exceptions.
         * */
        public void setItem(NodeItem nodeItem){
            Log.d("Set_Item", nodeItem.toString());
            this.nodeItem = nodeItem;
            try{
                nameTextView.setText(nodeItem.name);
                kindTextView.setText(nodeItem.kind);
            }catch (NullPointerException e){
                Log.e("Setting",e.toString());
                nameTextView.setText("NULL");
                kindTextView.setText("NULL");
            }

        }

        /**
         * Return the current node item
         * */
        public NodeItem getNodeItem(){ return nodeItem; }
    }
}
