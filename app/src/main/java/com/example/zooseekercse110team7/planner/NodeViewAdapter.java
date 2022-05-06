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
        private NodeItem nodeItem;
        private final TextView nameTextView, kindTextView;
        private final Button deleteBtn;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            nameTextView = itemView.findViewById(R.id.node_name_tv);//note: 'tv' means 'text view'
            kindTextView = itemView.findViewById(R.id.node_kind_tv);
            deleteBtn = itemView.findViewById(R.id.node_delete_btn);
        }

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

        public NodeItem getNodeItem(){ return nodeItem; }
    }
}
