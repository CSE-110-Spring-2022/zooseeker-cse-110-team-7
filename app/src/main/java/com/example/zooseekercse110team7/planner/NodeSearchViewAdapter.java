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

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class NodeSearchViewAdapter extends RecyclerView.Adapter <NodeSearchViewAdapter.ViewHolder>{
    private List<NodeItem> plannerItems = Collections.emptyList();
    private Consumer<NodeItem> onAddButtonClicked;

    public void setItems(List<NodeItem> newPlannerItems){
        plannerItems.clear();
        plannerItems = newPlannerItems;
        notifyDataSetChanged();
    }

    public void setOnAddButtonClicked(Consumer<NodeItem> onAddButtonClicked){
        this.onAddButtonClicked = onAddButtonClicked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //numberItemsTextView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_planner,null).findViewById(R.id.number_items_tv);
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.node_search_item, parent, false);
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
        private final TextView nameTextView, kindTextView/*, numberItemsTextView*/; // text views for `name` and `kind`
        private final Button addButton;

        //constructor
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            nameTextView = itemView.findViewById(R.id.node_name_tv2);//note: 'tv' means 'text view'
            kindTextView = itemView.findViewById(R.id.node_kind_tv2);
            addButton = itemView.findViewById(R.id.add_btn);
            //numberItemsTextView = itemView.findViewById(R.id.number_items_tv); // <-- will be null because it's in terms of `node_item.xml`
            addButton.setOnClickListener((view)->{
                if(onAddButtonClicked == null){ return; }
                onAddButtonClicked.accept(nodeItem);
            });
        }

        /**
         * Displays the exhibit the user wants to visit. It also prevents and Null Exceptions.
         * */
        public void setItem(NodeItem nodeItem){
            //Log.d("Set_Item", nodeItem.toString());
            //String count = "POI: " + String.valueOf(getItemCount());
            //setNumberItemsTextViewLog.d("Set_Count", count);
            this.nodeItem = nodeItem;
            try{
                nameTextView.setText(nodeItem.name);
                kindTextView.setText(nodeItem.kind);
                // numberItemsTextView.setText(count);
                // Log.d("Set_Count", "View Has: " + numberItemsTextView.getText());
            }catch (NullPointerException e){
                Log.e("Setting",e.toString());
                nameTextView.setText("NULL");
                kindTextView.setText("NULL");
//                numberItemsTextView.setText("NULL");
            }

        }

        /**
         * Return the current node item
         * */
        public NodeItem getNodeItem(){ return nodeItem; }
    }
}
