package com.example.zooseekercse110team7.planner;

import android.app.Activity;
import android.app.Application;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zooseekercse110team7.R;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

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
    private List<NodeItem> nodeItems = Collections.emptyList();
    private Consumer<NodeItem> onDeleteButtonClicked;

    /**
     * Similar to an observer, but allows an action to be performed. Here, it "deletes" a UI element
     * based on a `NodeItem`
     *
     * @param onDeleteButtonClicked a `NodeItem` data from which a UI element was clicked
     * */
    public void setOnDeleteButtonClicked(Consumer<NodeItem> onDeleteButtonClicked) {
        this.onDeleteButtonClicked = onDeleteButtonClicked;
    }

    /**
     * Sets a list of new `nodeItems` for the `ViewHolder` to use
     *
     * @param newItems list of new `nodeItems` to use
     * */
    public void setItems(List<NodeItem> newItems){
        nodeItems.clear();
        nodeItems = newItems;
        notifyDataSetChanged();
    }

    /**
     * Creates a `ViewHolder` with xml item that will be within the `ViewHolder`
     *
     * @param parent layout xml
     * @param viewType *Unknown*
     *
     * @return a `ViewHolder` with defined children templates
     * */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.node_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Allows you to more easily write code that interacts with views within the `ViewHolder`.
     * Here, you can figure out what the user has interacted with by getting the position of the
     * view element within the viewer.
     *
     * @param holder the current `ViewHolder` being inspected
     * @param position the Nth element within the viewer/Recycler
     * */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setItem(nodeItems.get(position));
    }

    /**
     * Returns the number of items currently in the viewhodler. It's context (not the object)
     * describes what it's counting (i.e number of items in the planner, or number of total items)
     *
     * @return an integer of the number of items on the `nodeItems` list
     * */
    @Override
    public int getItemCount() {
        return nodeItems.size();
    }

    /**
     * This subclass describes an item view and metadata about its place within the RecyclerView
     * */
    public class ViewHolder extends RecyclerView.ViewHolder{
        private NodeItem nodeItem;      // current node item -- useful if helper functions used
        private final TextView
                nameTextView,           // text view for the name of a node
                kindTextView;           // text view for the kind node it is (i.e "exhibit")
        private final CheckBox checkBox;// check box to remove item to planner

        //constructor
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            nameTextView = itemView.findViewById(R.id.node_name_tv);//note: 'tv' means 'text view'
            kindTextView = itemView.findViewById(R.id.node_kind_tv);
            checkBox = itemView.findViewById(R.id.checkBox);

            //check box listener
            checkBox.setOnCheckedChangeListener((view, isChecked) -> {
                if(onDeleteButtonClicked == null){ return; }
                onDeleteButtonClicked.accept(nodeItem);
            });
        }

        /**
         * Displays the exhibit the user wants to visit. It also prevents and Null Exceptions.
         * */
        public void setItem(NodeItem nodeItem){
            Log.d("NodeSearchAdapter", "Attempting to set items");
            this.nodeItem = nodeItem;
            try {
                checkBox.setChecked(!nodeItem.onPlanner);
                nameTextView.setText(nodeItem.name);
                kindTextView.setText(nodeItem.kind);
                Log.d("NodeAdapter", "|-> Items Set");
            }catch (NullPointerException e){
                Log.e("NodeAdapter", "|-> ERROR: Items Not Set");
                e.printStackTrace();
            }

        }

        /**
         * Return the current node item
         * */
        public NodeItem getNodeItem(){ return nodeItem; }
    }
}
