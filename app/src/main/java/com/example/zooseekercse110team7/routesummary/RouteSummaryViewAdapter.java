package com.example.zooseekercse110team7.routesummary;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zooseekercse110team7.R;
import com.example.zooseekercse110team7.map.IdentifiedWeightedEdge;
import com.example.zooseekercse110team7.planner.NodeItem;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.Collections;
import java.util.List;

public class RouteSummaryViewAdapter extends RecyclerView.Adapter <RouteSummaryViewAdapter.ViewHolder>{

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
    public RouteSummaryViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.route_summary_item, parent, false);
        return new RouteSummaryViewAdapter.ViewHolder(view);
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
    public void onBindViewHolder(@NonNull RouteSummaryViewAdapter.ViewHolder holder, int position) {
        holder.setItem(RouteSummary.getInstance().getRouteItem(position));
    }

    /**
     * Returns the number of items currently in the viewhodler. It's context (not the object)
     * describes what it's counting (i.e number of items in the planner, or number of total items)
     *
     * @return an integer of the number of items on the `nodeItems` list
     * */
    @Override
    public int getItemCount() {
        return RouteSummary.getInstance().getItems().size();
    }
    public void notifyView() {
        Log.d("RouteSummaryViewAdapter", "Notified of Change");
        notifyDataSetChanged();
    }
    /**
     * This subclass describes an item view and metadata about its place within the RecyclerView
     * */
    public class ViewHolder extends RecyclerView.ViewHolder{
        private RouteItem routeItem;      // current node item -- useful if helper functions used
        private final TextView
                toExhibitTextView,
                fromExhibitTextView,
                distanceTextView;

        //constructor
        public ViewHolder(@NonNull View itemView){
            super(itemView);

            //note: 'tv' means 'text view'
            toExhibitTextView   = itemView.findViewById(R.id.to_exhibit_tv);
            fromExhibitTextView = itemView.findViewById(R.id.from_exhibit_tv);
            distanceTextView    = itemView.findViewById(R.id.distance_tv);
        }

        /**
         * Displays the exhibit the user wants to visit. It also prevents and Null Exceptions.
         * */
        public void setItem(RouteItem routeItem){
            Log.d("RouteSummaryViewAdapter", "Attempting to set item");
            this.routeItem = routeItem;
            Log.d("RouteSummaryViewAdapter", routeItem.toString());
            try{
                toExhibitTextView.setText(routeItem.toExhibitName);
                fromExhibitTextView.setText(routeItem.fromExhibitName);
                distanceTextView.setText(routeItem.distance);
                Log.d("RouteSummaryAdapter", "|-> Items Set");
            }catch (NullPointerException e){
                Log.e("RouteSummaryAdapter", "|-> ERROR: Items Not Set");
                e.printStackTrace();
            }

        }
    }
}
