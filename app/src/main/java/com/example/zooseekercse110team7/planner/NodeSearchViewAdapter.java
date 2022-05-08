package com.example.zooseekercse110team7.planner;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zooseekercse110team7.R;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class NodeSearchViewAdapter extends RecyclerView.Adapter <NodeSearchViewAdapter.ViewHolder>{
    private List<NodeItem> nodeItems = Collections.emptyList();

    public void setItems(List<NodeItem> newItems){
        nodeItems.clear();
        nodeItems = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.node_search_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NodeItem nodeItem = nodeItems.get(position);
        if (nodeItem.onPlanner) {
            holder.checkBox.setChecked(true);
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBox.isChecked()) {
                    nodeItem.onPlanner = true;
                } else {
                    nodeItem.onPlanner = false;
                }
            }
        });
        holder.setItem(nodeItem);
    }

    @Override
    public int getItemCount() {
        return nodeItems.size();
    }

//    @Override
//    public long getItemId(int position){ return plannerItems.get(position).id; }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private NodeItem nodeItem;      // current node item -- useful if helper functions used
        private final TextView nameTextView, kindTextView/*, numberItemsTextView*/; // text views for `name` and `kind`
        public final CheckBox checkBox;

        //constructor
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            nameTextView = itemView.findViewById(R.id.node_name_tv2);//note: 'tv' means 'text view'
            kindTextView = itemView.findViewById(R.id.node_kind_tv2);
            checkBox = itemView.findViewById(R.id.checkBox);
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
