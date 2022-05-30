package com.example.zooseekercse110team7.map_v2;

import static java.lang.Math.sqrt;

import android.app.Activity;

import com.example.zooseekercse110team7.MapsActivity;
import com.example.zooseekercse110team7.location.Coord;
import com.example.zooseekercse110team7.planner.NodeDatabase;
import com.example.zooseekercse110team7.planner.NodeItem;
import com.example.zooseekercse110team7.planner.ReadOnlyNodeDao;
import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.ArrayList;
import java.util.List;

/**
 * A singleton class that provides information on the user's current location,
 * checks if they are off route, and determines the closest exhibit to the user.
 *
 * Requires that a reference to MapsActivity be passed in since the code acquiring the user's location is stored there, in addition to the
 * ReadOnlyNodeDao and NodeDatabase from there.
 * Example: Coord example = UserLocation.getInstance(this, nodeDao, db).getLocationCoordinates();
 * */
public class UserLocation extends Activity{
    private static UserLocation instance;

    ReadOnlyNodeDao nodeDao;
    NodeDatabase db;
    FusedLocationProviderClient mFusedLocationClient;

    int PERMISSION_ID = 44;
    double latitude;
    double longitude;
    MapsActivity myMapsActivity;


    private UserLocation(MapsActivity act, ReadOnlyNodeDao nodeDao, NodeDatabase db){
        this.db = db;
        this.nodeDao = nodeDao;
        this.myMapsActivity = act;
    }

    public static UserLocation getInstance(MapsActivity act, ReadOnlyNodeDao nodeDao, NodeDatabase db){
        if (instance == null){
            instance = new UserLocation(act, nodeDao, db);
        }

        return instance;
    }

    /**
     * @pre non null requirements
     * */
    public static UserLocation getInstance(){ return instance; }

    //The distance formula. Used in getClosestExhibit().
    public double distanceFormulaHelper(double firstLatitude, double firstLongitude, double secondLatitude, double secondLongitude){
        double changeInLat = (secondLatitude - firstLatitude);
        double changeInLong = (secondLongitude - firstLongitude);
        double distance = sqrt((changeInLat * changeInLat) + (changeInLong * changeInLong));
        return distance;
    }

    //Method that compares the user's current coordinates with every other exhibit's coordinates to return the closest exhibit (in the form of its ID) to the user.
    public String getClosestExhibit(){
        double closestDistance;
        NodeItem closestExhibit;

        //Get the list of all exhibits in the zoo
        List<NodeItem> exhibits = nodeDao.getAll();
        for(int i = exhibits.size() - 1; i >= 0; i--){
            if(!exhibits.get(i).kind.equals("exhibit")){
                exhibits.remove(i);
            }
        }

        //Get the user's current location

        Coord userCoords = getLocationCoordinates();
        double userLatitude = userCoords.lat;
        double userLongitude = userCoords.lng;

        //Set an initial 'closest' distance with the first item from exhibits list.
        closestDistance = distanceFormulaHelper(userLatitude, userLongitude, exhibits.get(0).lat, exhibits.get(0).lng);
        closestExhibit = exhibits.get(0);

        //Find the closest exhibit by comparing the above with every other exhibit.
        for(int i = 1; i < exhibits.size(); i++){
            double comparingDistance = distanceFormulaHelper(userLatitude, userLongitude, exhibits.get(i).lat, exhibits.get(i).lng);
            //If the exhibit being compared with the current closest exhibit is closer to the user, update the closest exhibit to that.
            if(comparingDistance < closestDistance){
                closestExhibit = exhibits.get(i);
                closestDistance = comparingDistance;
            }
        }


        //return "Default";
        //Return the closest exhibit's ID.
        return closestExhibit.id;
    }

    public boolean checkForReroute(){
        //NodeDatabase db = NodeDatabase.getSingleton(getApplicationContext());
        //nodeDao = db.nodeDao();

        //Get the list of exhibits on the planner.
        List<NodeItem> exhibits = nodeDao.getByOnPlanner(true);

        //Get the IDs of the above exhibits into a list.
        List<String> exhibitIDs = new ArrayList<String>();
        for(NodeItem x : exhibits){
            exhibitIDs.add(x.id);
        }

        //Get the exhibit closest to the user.
        String closestExhibit = getClosestExhibit();

        //Check if the closest exhibit to the user is on the prescribed path. If so, return false, indicating no need for a reroute.
        if(exhibitIDs.contains(closestExhibit)){
            return false;
        }

        //If the closest exhibit is not on the prescribed route, return true, indicating a need to reroute.
        return true;
    }




    /**
     * Returns a Coord consisting of latitude and longitude coordinates.
     *
     * */
    public Coord getLocationCoordinates(){
        //mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //getLastLocation();
        //MapsActivity.Pair<Double,Double> coordinates = new MapsActivity.Pair(latitude, longitude);
        //return coordinates;
        //MapsActivity r = new MapsActivity();
        //r.onSearchClicked();

        Coord userCoords = myMapsActivity.latLng();
        return userCoords;
    }

    /**
     * Everything below here is part of getting user's current location.
     * */
    /*
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

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
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

     */


}
