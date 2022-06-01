package com.example.zooseekercse110team7;



import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;


import android.app.AlertDialog;
import android.content.DialogInterface;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;

import android.os.Handler;
import android.text.InputType;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.zooseekercse110team7.location.Coord;
import com.example.zooseekercse110team7.location.MockLocationParser;
import com.example.zooseekercse110team7.map_v2.AssetLoader;
import com.example.zooseekercse110team7.map_v2.MapGraph;
import com.example.zooseekercse110team7.map_v2.Path;

import com.example.zooseekercse110team7.map_v2.UserLocation;
import com.example.zooseekercse110team7.planner.NodeDatabase;
import com.example.zooseekercse110team7.planner.ReadOnlyNodeDao;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import com.example.zooseekercse110team7.map_v2.PrettyDirections;
import com.example.zooseekercse110team7.planner.NodeDaoRequest;
import com.example.zooseekercse110team7.routesummary.RouteItem;

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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Collections;
import java.util.List;

/**
 * This Activity handles everything related to the maps/directions. Because every part of the app is
 * focused on directions, this has basically become our main activity, but that is just a side
 * affect and not a conscious decision to break SRP
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
    ReadOnlyNodeDao nodeDao;
    AssetLoader assetLoader;


    //Location variables
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    double latitude;
    double longitude;
    NodeDatabase db;

    private Marker mapCenter;
    private boolean hasDeniedReroute;
    private boolean responseReceived = true; //initially set to true
    private List<Coord> mockUserLocations = Collections.emptyList(); //list inputted from MOCK button read in locations from GSON
    public boolean MOCKING_ON = false;
    private int location_index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        loadRequirements();
        setViewListeners();

        // [START_EXCLUDE silent]

        // [END_EXCLUDE]

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);


    }

    /**
     * This functions loads any of the requirements for any of the singletons needed within this
     * activity. Because some of the singletons depend on one another, there is a hierarchical order
     * of precedence.
     * NodeDatabase <-- NodeDao <-- *Any*
     * */
    private void loadRequirements(){
        db = NodeDatabase.getSingleton(getApplicationContext());
        nodeDao = db.nodeDao();

        Path.getInstance().getShortestPath(
                nodeDao.getByOnPlanner(true)
        );//on startup get planner info

        NodeDaoRequest.getInstance().setNodeDao(getApplicationContext());
        PrettyDirections.getInstance().setNodeDao(getApplicationContext());

        //Part of user location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        UserLocation.getInstance(this, nodeDao, db); // <--- instantiated

        assetLoader = AssetLoader
                .getInstance()
                .loadAssets(
                        "sample_zoo_graph.json",
                        "sample_node_info.json",
                        "sample_edge_info.json",
                        getApplicationContext()
                );                       // parses JSON files

    }

    /**
     * This function sets listeners to view objects within the MapsActivity XML file. However, this
     * mainly applies to a short amount of steps to occur on an event. Events that require a several
     * steps use a function call such as `onClickedNext()` which is a large function.
     * */
    private void setViewListeners(){
        /* ROW 1 */
        {
            findViewById(R.id.planner_button).setOnClickListener(view -> {
                // go to planner activity
                Intent intent = new Intent(MapsActivity.this, PlannerActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            });

            findViewById(R.id.search_button).setOnClickListener(view -> {
                // go to search activity
                Intent intent = new Intent(MapsActivity.this, PlannerActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            });

            findViewById(R.id.mock_btn).setOnClickListener(view -> {
                Log.d("ViewListeners", "Mock Button Clicked");
                MOCKING_ON= true;
                location_index = 0;
                readInPathText();
            });
        }

        /* ROW 2*/
        {
            findViewById(R.id.mock_next_btn).setOnClickListener(view -> {
                Log.d("ViewListeners", "Next Mock Button Clicked");
                if (MOCKING_ON && location_index < mockUserLocations.size() - 1) {
                    location_index++;
                    Log.d("MOCK NEXT", "MOCK_ON " + MOCKING_ON);
                    Log.d("MOCK NEXT", "Location_index: " + location_index + "Location: " +
                            mockUserLocations.get(location_index));
                    Log.d("MOCK NEXT", "Current user location: " + getUserLocation().toString());
                }
            });

            findViewById(R.id.mock_back_btn).setOnClickListener(view -> {
                Log.d("ViewListeners", "Back Mock Button Clicked");
                if (MOCKING_ON && location_index > 0) {
                    location_index--;
                    Log.d("MOCK NEXT", "MOCK_ON " + MOCKING_ON);
                    Log.d("MOCK NEXT", "Location_index: " + location_index + "Location: " +
                            mockUserLocations.get(location_index));
                    Log.d("MOCK NEXT", "Current user location: " + getUserLocation().toString());
                }
            });

            findViewById(R.id.turn_off_mocking_btn).setOnClickListener(view -> {
                Log.d("ViewListeners", "Mock Off Button Clicked");
                MOCKING_ON = false;
                if(mockUserLocations == null || mockUserLocations.isEmpty()){ return; }
                location_index = 0;
                Log.d("MOCK OFF", "MOCK_ON " + MOCKING_ON);
                Log.d("MOCK OFF", "Location_index: " + location_index + "Location: " +
                        mockUserLocations.get(location_index));
                Log.d("MOCK OFF", "Current user location: " + getUserLocation().toString());
            });
        }

        /* ROW 3 */
        {
            findViewById(R.id.reroute_btn).setOnClickListener(view -> {
                Log.d("ViewListeners", "Mock Button Clicked");
                reroutePath();
            });
        }

        /* ROW 4 */
        {
//            findViewById(R.id.next_directions_btn).setOnClickListener(view -> {
//
//            });
//
//            findViewById(R.id.skip_directions_btn).setOnClickListener(view -> {
//
//            });
//
//            findViewById(R.id.back_button).setOnClickListener(view -> {
//
//            });
        }
    }

    /**
     * Retrieves the user's current location. If locations are being mocked, then it will return
     * the mocked location rather than the actual location of the user.
     *
     * @return a coordinate object `Coord` of the user's current (and potentially mocked) location
     * */
    public Coord getUserLocation(){
        if (MOCKING_ON && mockUserLocations != null && !mockUserLocations.isEmpty()) {
            //return mocked location
            return mockUserLocations.get(location_index);
        }
        else {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            getLastLocation();
            Coord currentCoords = new Coord(latitude, longitude);

            //return actual location
            return currentCoords;
        }
    }

    /**
     * creates an alert dialog when the `Mock` button is pressed
     * if OK is pressed, the alert dialog reads inputed text and then stores it in the path_url string
     * else if Cancel is pressed, the alert dialog closes and the path_url remains unchanged.
     * https://stackoverflow.com/questions/10903754/input-text-dialog-android
     */
    private void readInPathText() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Location Mock JSON URL");

        // Set up the input
        final EditText input = new EditText(this);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String path_url = input.getText().toString();
                mockUserLocations = MockLocationParser.parseMockLocations(getApplicationContext(), path_url);
                if (GlobalDebug.DEBUG) {
                    for (Coord coor : mockUserLocations) {
                        Log.d("List<Coors>", coor.toString());
                    }
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    /**
     * Called when Directions is clicked. It displays directions from the user's current
     * position to the next item in the planner/list.
     *
     * @param view button of the item being clicked on
     * */
    public void onNextClicked(View view) {
        Log.d("MapsActivity", "Next Button Clicked!");

        /* GET DIRECTIONS */
        StringBuilder directions = new StringBuilder("[Next]\n");
        List<String> route = MapGraph.getInstance().getNextDirections();
        for(String detail: route){
            directions.append(detail);
        }

        /* SET DIRECTIONS TEXT */
        TextView directionsTextview =
                (TextView) findViewById(R.id.directions_text); // text view to display directions
        directionsTextview.setText(directions.toString());
        Log.d("MapsActivity", "[Next Directions Updated]\n" + directions);
    }

    /**
     * Called when Directions is clicked. It displays directions from the user's current
     * position to the previous item in the planner/list.
     *
     * @param view button of the item being clicked on
     * */
    public void onBackClicked(View view){
        Log.d("MapsActivity", "Back Button Clicked!");

        /* GET DIRECTIONS */
        StringBuilder directions = new StringBuilder("[Back]\n");
        List<String> route = MapGraph.getInstance().getPreviousDirections();
        for(String detail: route){
            directions.append(detail);
        }

        /* SET DIRECTIONS TEXT */
        TextView directionsTextview =
                (TextView) findViewById(R.id.directions_text); // text view to display directions
        directionsTextview.setText(directions.toString());
        Log.d("MapsActivity", "[Back Directions Updated]" + directions);
    }


    /**
     * Called when Directions is clicked. It displays directions from the user's current
     * position to the second next or previous item in the planner/list removing the first next or
     * previous item in the list. it then reroutes the remaining items to visit if skipping the next
     * item in the list. Otherwise it connects to second previous item to user's current location.
     *
     * @param view button of the item being clicked on
     * */
    public void onSkipClicked(View view){
        String itemId = MapGraph.getInstance().getCurrentItemToVisitId();
        if(null == itemId){
            Log.d("MapsActivity", "Cannot Find Skipable Item! -- Returning");
            return;
        }
        boolean updateSuccess = NodeDaoRequest.getInstance()
                .setContext(getApplicationContext())
                .RequestPlannerSkip(itemId);

        if(!updateSuccess){
            Log.d("MapsActivity", "Skip Item Failed! -- Returning");
            return;
        }

        //update path relative to current source
        MapGraph.getInstance().updatePathWithRemovedItem();

        //update view/text
        StringBuilder directions = new StringBuilder("[Updated After Skip]\n");
        List<String> route = MapGraph.getInstance().getCurrentDirections();
        for(String detail: route){
            directions.append(detail);
        }

        Log.d("MapsActivity", "[Skip]\n" + directions);
        TextView directionsTextview =
                (TextView) findViewById(R.id.directions_text); // text view to display directions
        directionsTextview.setText(directions.toString());

    }

    /**
     * Called when Refresh Directions is clicked. It gets the current directions and displays it to
     * the user.
     *
     * @param view button of the item being clicked on
     * */
    public void onRefreshClicked(View view){
        /* GET DIRECTIONS */
        StringBuilder directions = new StringBuilder("[Updated]\n");
        List<String> route = MapGraph.getInstance().getCurrentDirections();
        for(String detail: route){
            directions.append(detail);
        }

        /* SET DIRECTIONS */
        Log.d("MapsActivity", "[Updated]\n" + directions);
        TextView directionsTextview =
                (TextView) findViewById(R.id.directions_text); // text view to display directions
        directionsTextview.setText(directions.toString());
    }


    /**
     * Changes how brief the directions are when the switch is toggled.
     *
     * @param view button of the item being clicked on
     * */
    public void onBriefDirectionsSwitch(View view){
        Log.d("MapsActivity", "Toggled Brief Directions!");

        /* GET DIRECTIONS */
        MapGraph.getInstance().updateDirectionsBrevity();
        StringBuilder directions = new StringBuilder("[Updated Brevity of Directions]\n");
        List<String> route = MapGraph.getInstance().getCurrentDirections();
        for(String detail: route){
            directions.append(detail);
        }

        /* SET DIRECTIONS */
        Log.d("MapsActivity", "[On Brief]\n" + directions);
        TextView directionsTextview =
                (TextView) findViewById(R.id.directions_text); // text view to display directions
        directionsTextview.setText(directions.toString());
    }

    // [<REROUTE HANDLER>]


    /**
     * https://stackoverflow.com/questions/2478517/how-to-display-a-yes-no-dialog-box-on-android
     * Creates a dialog box that asks user if they want to reroute or not.
     */
    private void askUserToReroute () {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Rerouting");
        builder.setMessage("You are off the path, would you like to reroute?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                reroutePath();
                responseReceived = true;
                dialog.dismiss();

            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                hasDeniedReroute = true;
                responseReceived = true;
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
    boolean isUserOffRoute() {
        return UserLocation.getInstance(this, nodeDao, db).checkForReroute();
    }
    String getClosestExhibit() {
        return UserLocation.getInstance(this, nodeDao, db).getClosestExhibit();
    }

    /**
     * Checks if user is going `forward` and user is off-route
     * Queries user if they want to reroute for a more optimal path
     */
    public void checkOffPath() {
        MapGraph graph = MapGraph.getInstance();

        //resets because they are back on track
        if (hasDeniedReroute && !isUserOffRoute()) {
            hasDeniedReroute = false;
        }
        if (!hasDeniedReroute && !isUserOffRoute() && !graph.isGoingBackwards() && responseReceived) {
            responseReceived = false;
            askUserToReroute();
        }

    }

    /**
     * Uses the graph methods to recreate a new route based off the closest exhibit
     * @ensure the path is going forward
     */
    private void reroutePath() {
        MapGraph graph = MapGraph.getInstance();
        List<RouteItem> routeItemsToVisit = graph.getRemainingSubpathList();
        List<RouteItem> routeItemsVisited = graph.getVisitedSubpathList();
        if (routeItemsVisited.size() >= 1) {
            String closestExhibit = getClosestExhibit();
            List<RouteItem> newRoute = Path.getInstance().notUpdateGraph().getShortestPath(closestExhibit, routeItemsToVisit, Path.getInstance().getDEFAULT_DESTINATION());
            routeItemsVisited.addAll(newRoute);
            graph.setPath(routeItemsVisited);
        }
    }
    /**
     * onResume, rerouter checks if delay (15 seconds) past before running handleRerouting
     * onPause, rerouter pauses until activity is resumed (doesn't check for off route)
     * https://stackoverflow.com/questions/11434056/how-to-run-a-method-every-x-seconds
     */
    Handler rerouter = new Handler();
    Runnable runnable;
    int delay = 15*1000; //Delay for 15 seconds.  One second = 1000 milliseconds.
    @Override
    protected void onResume() {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(this).edit();
        e.putString("last_activity", getClass().getSimpleName());
        e.commit();
        //location stuff
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

        //start handler as activity become visible

        rerouter.postDelayed( runnable = new Runnable() {
            public void run() {
                //call location functions

                checkOffPath();

                rerouter.postDelayed(runnable, delay);
            }
        }, delay);

        super.onResume();
    }

    // If onPause() is not included the threads will double up when you
    // reload the activity

    @Override
    protected void onPause() {
        rerouter.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }
    // [</REROUTE HANDLER>]




    // [START_EXCLUDE silent]

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
        if (mapCenter != null)
            mapCenter.remove();
        mapCenter = map.addMarker(new MarkerOptions()
                .position(target)
                .title("You"));
//        currPolylineOptions.add(target);
    }


    //START


    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest object with appropriate methods
        LocationRequest mLocationRequest =  LocationRequest.create()
                .setInterval(100)
                .setFastestInterval(3000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(100);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }
}
