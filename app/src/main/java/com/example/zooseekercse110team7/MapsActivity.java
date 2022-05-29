package com.example.zooseekercse110team7;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.zooseekercse110team7.map_v2.AssetLoader;
import com.example.zooseekercse110team7.depreciated_map.CalculateShortestPath;
import com.example.zooseekercse110team7.map_v2.MapGraph;
import com.example.zooseekercse110team7.map_v2.Path;
import com.example.zooseekercse110team7.map_v2.PrettyDirections;
import com.example.zooseekercse110team7.planner.NodeDatabase;
import com.example.zooseekercse110team7.planner.NodeItem;
import com.example.zooseekercse110team7.planner.ReadOnlyNodeDao;
import com.example.zooseekercse110team7.planner.UpdateNodeDaoRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveCanceledListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

/**
 * This shows how to change the camera position for the map.
 */
// [START maps_camera_events]
public class MapsActivity extends AppCompatActivity implements
        OnCameraMoveStartedListener,
        OnCameraMoveListener,
        OnCameraMoveCanceledListener,
        OnCameraIdleListener,
        OnMapReadyCallback {
    // [START_EXCLUDE silent]
    private static final String TAG = MapsActivity.class.getName();


    /**
     * The amount by which to scroll the camera. Note that this amount is in raw pixels, not dp
     * (density-independent pixels).
     */
    private static final int SCROLL_BY_PX = 100;

    public static final CameraPosition BONDI =
            new CameraPosition.Builder().target(new LatLng(-33.891614, 151.276417))
                    .zoom(15.5f)
                    .bearing(300)
                    .tilt(50)
                    .build();

    public static final CameraPosition ZOO =
            new CameraPosition.Builder().target(new LatLng(32.735294, -117.14931))
                    .zoom(15.5f)
                    .bearing(0)
                    .tilt(25)
                    .build();
    // [END_EXCLUDE]

    private GoogleMap map;
    private PolylineOptions currPolylineOptions;
    private boolean isCanceled = false;
    private CalculateShortestPath directions;
    private List<NodeItem> plannedItems;
    ReadOnlyNodeDao nodeDao;
    AssetLoader g;
    int startCounter = 0;
    int goalCounter = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // [START_EXCLUDE silent]

        // [END_EXCLUDE]

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        /**
         * [Note]: Loading the Assets Takes Precedence!
         * Dependency: AssetLoader <-- MapGraph <-- Path
         * */
        g = AssetLoader
                .getInstance()
                .loadAssets(
                "sample_zoo_graph.json",
                "sample_node_info.json",
                "sample_edge_info.json",
                getApplicationContext());                       // parses JSON files

        NodeDatabase db = NodeDatabase.getSingleton(getApplicationContext());
        nodeDao = db.nodeDao();
        Path.getInstance().getShortestPath(nodeDao.getByOnPlanner(true));//on startup get planner info
        PrettyDirections.getInstance().setContext(getApplicationContext());
    }

    // Called when Directions is clicked. Displays directions for pairs of destinations in order
    // each time it's clicked.
    public void onNextClicked(View view) {
        Log.d("MapsActivity", "Next Button Clicked!");
        TextView directionsTextview =
                (TextView) findViewById(R.id.directions_text); // text view to display directions

        String directions = "[Next]\n";
        List<String> route = MapGraph.getInstance().getNextDirections();

        for(String detail: route){
            directions += detail;
        }

        directionsTextview.setText(directions);
        Log.d("MapsActivity", "[Next Directions]" + directions);
        Log.d("MapsActivity", "Next Updated!");
    }

    /**
     * https://piazza.com/class/l186r5pbwg2q4?cid=648
     * 1. Does hitting the previous button backtrace and reverse the steps? For example, the user
     * story mentions 2 scenarios - pressing previous to find out how to backtrack to the hippos
     * exhibit, but also pressing next to "preview" the next exhibit and then pressing previous to
     * return to the current one.
     *
     * This was -- partially -- previously asked and answered in previous clarifications. I recommend
     * you read those responses carefully. In general, the answer is, yes, Previous reverses the
     * steps. It's even in the Scenario.However, directions are now from your actual location
     * as opposed to a supposed location.
     * */
    public void onBackClicked(View view){
        Log.d("MapsActivity", "Back Button Clicked!");
        TextView directionsTextview =
                (TextView) findViewById(R.id.directions_text); // text view to display directions

        String directions = "[Back]\n";
        List<String> route = MapGraph.getInstance().getPreviousDirections();
        for(String detail: route){
            directions += detail;
        }

        directionsTextview.setText(directions);
        Log.d("MapsActivity", "[Back Directions]" + directions);
        Log.d("MapsActivity", "Back Updated!");
    }


    //What happens if you use default end and start, do you remove them?
    // -- start and ends of a path aren't deleted! so this may be a double edged sword
    //
    public void onSkipClicked(View view){
        String itemId = MapGraph.getInstance().getCurrentItemToVisitId();
        if(null == itemId){ return; }
        boolean updateSuccess = UpdateNodeDaoRequest.getInstance()
                .setContext(getApplicationContext())
                .RequestPlannerSkip(itemId);

        if(!updateSuccess){
            Log.d("MapsActivity", "Skip Item Failed! -- Returning");
            return;
        }

        //update path relative to current source
        MapGraph.getInstance().updatePathWithRemovedItem();

        //update view/text
        String directions = "[Updated After Skip]\n";
        List<String> route = MapGraph.getInstance().getCurrentDirections();
        for(String detail: route){
            directions += detail;
        }

        Log.d("MapsActivity", "[Skip]\n" + directions);
        TextView directionsTextview =
                (TextView) findViewById(R.id.directions_text); // text view to display directions
        directionsTextview.setText(directions);

    }


    public void onBriefDirectionsSwitch(View view){
        Log.d("MapsActivity", "Toggled Brief Directions!");
        MapGraph.getInstance().updateDirectionsBrevity();

        String directions = "[Updated to Brief Directions]\n";
        List<String> route = MapGraph.getInstance().getCurrentDirections();
        for(String detail: route){
            directions += detail;
        }

        Log.d("MapsActivity", "[Brief]\n" + directions);
        TextView directionsTextview =
                (TextView) findViewById(R.id.directions_text); // text view to display directions
        directionsTextview.setText(directions);
    }


    // [START_EXCLUDE silent]
    @Override
    protected void onResume() {
        super.onResume();
    }
    // [END_EXCLUDE]

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setOnCameraIdleListener(this);
        map.setOnCameraMoveStartedListener(this);
        map.setOnCameraMoveListener(this);
        map.setOnCameraMoveCanceledListener(this);
        // [START_EXCLUDE silent]
        // We will provide our own zoom controls.
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        // [END_EXCLUDE]

        // Show Zoo
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(32.735294, -117.14931), 17));

    }

    // [START_EXCLUDE silent]
    /**
     * When the map is not ready the CameraUpdateFactory cannot be used. This should be called on
     * all entry points that call methods on the Google Maps API.
     */
    private boolean checkReady() {
        if (map == null) {
            Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void onPlannerClicked(View view){
        Intent intent = new Intent(MapsActivity.this, PlannerActivity.class);
        startActivity(intent);
    }

    public void onSearchClicked(View view){
        Intent intent = new Intent(MapsActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    /**
     * Called when the Go To Bondi button is clicked.
     */
    /*
    public void onGoToBondi(View view) {
        if (!checkReady()) {
            return;
        }
        changeCamera(CameraUpdateFactory.newCameraPosition(ZOO));
    }
     */



    private void changeCamera(CameraUpdate update) {
        changeCamera(update, null);
    }

    /**
     * Change the camera position by moving or animating the camera depending on the state of the
     * animate toggle button.
     */
    private void changeCamera(CameraUpdate update, CancelableCallback callback) {
        map.animateCamera(update, callback);
        map.moveCamera(update);
    }
    // [END_EXCLUDE]

    @Override
    public void onCameraMoveStarted(int reason) {
        // [START_EXCLUDE silent]
        if (!isCanceled) {
            map.clear();
        }
        // [END_EXCLUDE]

        String reasonText = "UNKNOWN_REASON";
        // [START_EXCLUDE silent]
        currPolylineOptions = new PolylineOptions().width(5);
        // [END_EXCLUDE]
        switch (reason) {
            case OnCameraMoveStartedListener.REASON_GESTURE:
                // [START_EXCLUDE silent]
                currPolylineOptions.color(Color.BLUE);
                // [END_EXCLUDE]
                reasonText = "GESTURE";
                break;
            case OnCameraMoveStartedListener.REASON_API_ANIMATION:
                // [START_EXCLUDE silent]
                currPolylineOptions.color(Color.RED);
                // [END_EXCLUDE]
                reasonText = "API_ANIMATION";
                break;
            case OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION:
                // [START_EXCLUDE silent]
                currPolylineOptions.color(Color.GREEN);
                // [END_EXCLUDE]
                reasonText = "DEVELOPER_ANIMATION";
                break;
        }
        Log.d(TAG, "onCameraMoveStarted(" + reasonText + ")");
        // [START_EXCLUDE silent]
        addCameraTargetToPath();
        // [END_EXCLUDE]
    }

    @Override
    public void onCameraMove() {
        // [START_EXCLUDE silent]
        // When the camera is moving, add its target to the current path we'll draw on the map.
        if (currPolylineOptions != null) {
            addCameraTargetToPath();
        }
        // [END_EXCLUDE]
        Log.d(TAG, "onCameraMove");
    }

    @Override
    public void onCameraMoveCanceled() {
        // [START_EXCLUDE silent]
        // When the camera stops moving, add its target to the current path, and draw it on the map.
        if (currPolylineOptions != null) {
            addCameraTargetToPath();
            map.addPolyline(currPolylineOptions);
        }
        isCanceled = true;  // Set to clear the map when dragging starts again.
        currPolylineOptions = null;
        // [END_EXCLUDE]
        Log.d(TAG, "onCameraMoveCancelled");
    }

    @Override
    public void onCameraIdle() {
        // [START_EXCLUDE silent]
        if (currPolylineOptions != null) {
            addCameraTargetToPath();
            map.addPolyline(currPolylineOptions);
        }
        currPolylineOptions = null;
        isCanceled = false;  // Set to *not* clear the map when dragging starts again.
        // [END_EXCLUDE]
        Log.d(TAG, "onCameraIdle");
    }

    // [START_EXCLUDE silent]
    private void addCameraTargetToPath() {
        LatLng target = map.getCameraPosition().target;
        currPolylineOptions.add(target);
    }
    // [END_EXCLUDE]
}
// [END maps_camera_events]