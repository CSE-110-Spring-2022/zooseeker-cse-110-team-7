package com.example.zooseekercse110team7.planner;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zooseekercse110team7.R;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class FilterDialogViewAdapter extends RecyclerView.Adapter <FilterDialogViewAdapter.ViewHolder>{
    private List<FilterItem> kinds = Collections.emptyList();
    private NodeSearchViewModel viewModel;
    private Consumer<FilterItem> onUpdateFilters;

    //constructor -- sets view model
    public FilterDialogViewAdapter(NodeSearchViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Similar to an observer, but allows an action to be performed. Here, it updates the filters
     * so that they appear on search activity.
     *
     * @param onUpdateFilters a functor within the View Model to perform to update the data
     * */
    public void setOnUpdateFilters(Consumer<FilterItem> onUpdateFilters){
        this.onUpdateFilters = onUpdateFilters;
    }

    /**
     * Sets a list of new `nodeItems` for the `ViewHolder` to use
     *
     * @param newItems list of new `nodeItems` to use
     * */
    public void setItems(List<FilterItem> newItems){
        kinds.clear();
        kinds = newItems;
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
    public FilterDialogViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.filter_item, parent, false);
        return new FilterDialogViewAdapter.ViewHolder(view);
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
    public void onBindViewHolder(@NonNull FilterDialogViewAdapter.ViewHolder holder, int position) {
        FilterItem kindItem = kinds.get(position);
        holder.setItem(kindItem);
    }

    /**
     * Returns the number of items currently in the viewhodler. It's context (not the object)
     * describes what it's counting (i.e number of items in the planner, or number of total items)
     *
     * @return an integer of the number of items on the `nodeItems` list
     * */
    @Override
    public int getItemCount() {
        return kinds.size();
    }

    /**
     * This subclass describes an item view and metadata about its place within the RecyclerView
     * */
    public class ViewHolder extends RecyclerView.ViewHolder{
        private FilterItem kindItem;      // current node item -- useful if helper functions used
        private final TextView
                kindTextView;           // text view for the name of a node
        public final CheckBox checkBox; // check box to add item to planner

        //constructor
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            kindTextView = itemView.findViewById(R.id.filter_item_kind_tv);
            checkBox = itemView.findViewById(R.id.filter_item_checkbox);

            checkBox.setOnClickListener((view) -> {
                Log.d("FilterDialogViewAdapter", "| UI Update - Filter Changed");
                kindItem.setOnFilter(!kindItem.isOnFilter());// making this too complex, can just
                                                             // pass in an unaltered version of it
                                                             // and update it through the functor's
                                                             // method
                if(onUpdateFilters == null){ return; }
                Log.d("FilterDialogViewAdapter", "| Changes Will Begin To Process...");
                onUpdateFilters.accept(kindItem);
            });
        }

        /**
         * Displays the exhibit the user wants to visit. It also prevents and Null Exceptions.
         * */
        public void setItem(FilterItem kindItem){
            Log.d("FilterDialogViewAdapter", "Attempting to set items");
            this.kindItem = kindItem;
            try{
                kindTextView.setText(kindItem.getName());
                checkBox.setChecked(kindItem.isOnFilter());
                Log.d("FilterDialogViewAdapter", "|-> Items Set");
            }catch (NullPointerException e){
                Log.e("FilterDialogViewAdapter", "|-> ERROR: Items Not Set");
                e.printStackTrace();
            }
        }

        /**
         * Return the current node item the user interacted with
         *
         * @return a `NodeItem`
         * */
        public FilterItem getKindItem(){ return kindItem; }
    }

}
