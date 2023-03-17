package com.example.compassproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.compassproject.ViewModel.CompassViewModel;
import com.example.compassproject.model.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;

public class CompassActivity extends AppCompatActivity {
    private HashMap<String, View> locMap;

    private double currOrientation;
    private double latitude;
    private double longitude;
    private int radius;

    private ImageView compass;
    private TextView gpsIndicator;
    private TextView timeIndicator;
    SharedPreferences preferences;

    int zoomLevel;
    CompassViewModel viewModel;

    ArrayList<LiveData<Location>> locationArray;

    ConstraintLayout.LayoutParams north_lp, south_lp, east_lp, west_lp;
    ConstraintLayout.LayoutParams zoom_1_lp, zoom_2_lp, zoom_3_lp;
    TextView north, south, east, west;
    ImageView zoom_1, zoom_2, zoom_3;

    LocationService ls;

    boolean hasGPSFix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        // Create View Model and SharedPreferences
        viewModel = setupViewModel();
        preferences = getSharedPreferences("ZoomData", Context.MODE_PRIVATE);

        // Create ZoomService and save initial zoom level
        ZoomService zoomService = new ZoomService(this);
        zoomLevel = zoomService.getZoomLevel();

        // Create location and orientation services
        ls = LocationService.singleton(this);
        this.reobserveLocation();

        OrientationService os = OrientationService.singleton(this);

        // Create location map and array
        locMap = new HashMap<>();
        locationArray = new ArrayList<>();

        // Save GPS Indicator Views
        gpsIndicator = findViewById(R.id.statusIndicator);
        timeIndicator = findViewById(R.id.timeIndicator);

        // Make sure compass has been set up on UI
        compass = (ImageView) findViewById(R.id.compass_face);
        ViewTreeObserver observer = compass.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setupUI();

                // When our location changes, update to server and update friend's relative angles
                ls.getLocation().observe(CompassActivity.this, location -> {
                    updateCoordinates(location);
                    updateAllFriendLocations();
                });

                // Continuously update orientation data to local fields
                os.getOrientation().observe(CompassActivity.this, orientation -> {
                    updateOrientation(orientation);
                    updateCardinalAxisLabels();
                    updateAllFriendLocations();

                });

                // When our GPS status changes, update it on the UI
                ls.getGPSFix().observe(CompassActivity.this, hasGPS ->{
                    updateGPSStatus(hasGPS);
                });

                compass.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        // Save button views
        Button zoomInButton = findViewById(R.id.zoom_in_btn);
        Button zoomOutButton = findViewById(R.id.zoom_out_btn);

        // Setup zoom in button
        zoomInButton.setOnClickListener(v -> {
            zoomService.zoomIn();
            zoomLevel = zoomService.getZoomLevel();
            updateZoomButtonState();
            Log.d("Zoom Level", " "+ zoomLevel);
            updateZoomCircles();
            updateAllFriendLocations();
        });

        // Setup zoom out button
        zoomOutButton.setOnClickListener(v -> {
            zoomService.zoomOut();
            zoomLevel = zoomService.getZoomLevel();
            updateZoomButtonState();
            Log.d("Zoom Level", " "+ zoomLevel);
            updateZoomCircles();
            updateAllFriendLocations();
        });
    }

    // Disable zoom buttons when appropriate
    private void updateZoomButtonState() {
        Button zoomInButton = findViewById(R.id.zoom_in_btn);
        Button zoomOutButton = findViewById(R.id.zoom_out_btn);
        if (zoomLevel == 1) {
            zoomInButton.setEnabled(false);
        } else {
            zoomInButton.setEnabled(true);
        }
        if (zoomLevel == 4) {
            zoomOutButton.setEnabled(false);
        } else {
            zoomOutButton.setEnabled(true);
        }
    }

    // Update UI based on GPS status
    private void updateGPSStatus(Boolean hasGPS) {
        if(hasGPS){
            setGPSStatusTrue();
        }
        else{
            setSetGPSStatusFalse(ls.getElapsedTime());
        }

        hasGPSFix = hasGPS;
    }

    // Change UI to indicate live GPS connection
    public void setGPSStatusTrue(){
        gpsIndicator.setTextColor(Color.GREEN);
        timeIndicator.setText("Live");
    }

    // Change UI to indicate lack of GPS connection
    public void setSetGPSStatusFalse(long time){
        gpsIndicator.setTextColor(Color.RED);
        timeIndicator.setText(Utilities.formatElapsedTime(time));
    }

    // Update the UI with the latest location data for all friends
    public void updateAllFriendLocations(){
        // showing red dots which is saved user locations
        for(int i = 0; i < locationArray.size(); i++){
            LiveData<Location> currLocLive = locationArray.get(i);
            Location currLoc = currLocLive.getValue();
            updateFriendLocations(currLoc);
        }
    }

    // Update coordinates both locally and remotely
    public void updateCoordinates(Pair<Double, Double> location) {
        updateCoordinatesLocal(location);
        updateCoordinatesRemote(location);
    }

    // Update coordinates locally
    private void updateCoordinatesLocal(Pair<Double, Double> location){
        latitude = location.first;
        longitude = location.second;
        Log.i("CURR LOCATION", "(" + latitude + "," + longitude + ")");
    }

    // Update my coordinates on the server
    private void updateCoordinatesRemote(Pair<Double, Double> location){
        UserInfo u1 = new UserInfo(getSharedPreferences(getString(R.string.saveUserInfo), MODE_PRIVATE));
        viewModel.updateCoordinatesRemote(location, u1);
    }

    // Set up the view model
    private CompassViewModel setupViewModel() {
        return new ViewModelProvider(this).get(CompassViewModel.class);
    }

    // Set up the UI initially
    private void setupUI() {
        setupCardinalAxisLabels();
        setUpZoomCircles();
        setupFriendLocations();
        updateZoomButtonState();
    }

    // Update the angle of the cardinal axis labels based on current orientation
    private void updateCardinalAxisLabels(){
        north_lp.circleAngle = (float) - currOrientation;
        east_lp.circleAngle = (float) (90 - currOrientation);
        south_lp.circleAngle = (float) (180 - currOrientation);
        west_lp.circleAngle = (float) (270 - currOrientation);

        // Place cardinal direction labels in correct location
        north.setLayoutParams(north_lp);
        east.setLayoutParams(east_lp);
        south.setLayoutParams(south_lp);
        west.setLayoutParams(west_lp);
    }

    // Setup axis labels initially
    private void setupCardinalAxisLabels(){
        // Access UI elements for cardinal directions
        north = (TextView) findViewById(R.id.compass_N);
        east = (TextView) findViewById(R.id.compass_E);
        south = (TextView) findViewById(R.id.compass_S);
        west = (TextView) findViewById(R.id.compass_W);

        setRadius();

        // Placing cardinal direction labels in correct initial location
        north_lp = (ConstraintLayout.LayoutParams) north.getLayoutParams();
        north_lp.circleRadius = radius;
        north.setLayoutParams(north_lp);

        east_lp = (ConstraintLayout.LayoutParams) east.getLayoutParams();
        east_lp.circleRadius = radius;
        east.setLayoutParams(east_lp);

        south_lp = (ConstraintLayout.LayoutParams) south.getLayoutParams();
        south_lp.circleRadius = radius;
        south.setLayoutParams(south_lp);

        west_lp = (ConstraintLayout.LayoutParams) west.getLayoutParams();
        west_lp.circleRadius = radius;
        west.setLayoutParams(west_lp);
    }

    // Calculate radius of compass on screen
    private void setRadius(){
        radius = compass.getHeight() / 2;
    }

    // Set up zoom circles to be modified later
    public void setUpZoomCircles() {
        zoom_1 = (ImageView) findViewById(R.id.zoom_circle_1);
        zoom_2 = (ImageView) findViewById(R.id.zoom_circle_2);
        zoom_3 = (ImageView) findViewById(R.id.zoom_circle_3);

        zoom_1_lp = (ConstraintLayout.LayoutParams) zoom_1.getLayoutParams();
        zoom_2_lp = (ConstraintLayout.LayoutParams) zoom_2.getLayoutParams();
        zoom_3_lp = (ConstraintLayout.LayoutParams) zoom_3.getLayoutParams();

        updateZoomCircles();
    }

    // Adapt zoom circles sizes and visibility based on zoom level
    public void updateZoomCircles() {
        int z_radius = radius -64;
        switch (zoomLevel) {
            case 1: // at dist 1
                zoom_1.setVisibility(View.INVISIBLE);
                zoom_2.setVisibility(View.INVISIBLE);
                zoom_3.setVisibility(View.INVISIBLE);

                break;
            case 2: //at dist 1-10
                zoom_1.setVisibility(View.VISIBLE);
                zoom_2.setVisibility(View.INVISIBLE);
                zoom_3.setVisibility(View.INVISIBLE);

                zoom_1_lp.width = (int) Math.ceil(2.0 * z_radius * (1.0/2));
                zoom_1.setLayoutParams(zoom_1_lp);

                break;
            case 3: //at dist 10-500
                zoom_1.setVisibility(View.VISIBLE);
                zoom_2.setVisibility(View.VISIBLE);
                zoom_3.setVisibility(View.INVISIBLE);

                zoom_1_lp.width = (int) Math.ceil(2.0 * z_radius * (1.0/3));
                zoom_1.setLayoutParams(zoom_1_lp);

                zoom_2_lp.width = (int) Math.ceil(2.0 * z_radius * (2.0/3));
                zoom_2.setLayoutParams(zoom_2_lp);
                break;
            case 4: //at dist 500+
                zoom_1.setVisibility(View.VISIBLE);
                zoom_2.setVisibility(View.VISIBLE);
                zoom_3.setVisibility(View.VISIBLE);

                zoom_1_lp.width = (int) Math.ceil(2.0 * z_radius * (1.0/4));
                zoom_1.setLayoutParams(zoom_1_lp);

                zoom_2_lp.width = (int) Math.ceil(2.0 * z_radius * (1.0/2));
                zoom_2.setLayoutParams(zoom_2_lp);

                zoom_3_lp.width = (int) Math.ceil(2.0 * z_radius * (3.0/4));
                zoom_3.setLayoutParams(zoom_3_lp);

                break;
        }
    }

    // Set up circles and labels for friend's location on UI
    private void setupFriendLocations(){
        List<String> friendList = viewModel.getAllFriendUIDs();
        int numFriends = friendList.size();

        // Create circles and labels for each friend
        for(int i = 0; i < numFriends; i++){
            // Get friend's latest location information
            LiveData<Location> currLocLive = viewModel.getLiveLocation(friendList.get(i));
            Location currLoc = currLocLive.getValue();

            // Create circle or label in the given angle
            View loc_view = DisplayHelper.displaySingleLocation(CompassActivity.this, 1, radius-64, getDegree(currLoc) , getDistance(currLoc), zoomLevel, currLoc.label, 0);

            // Save views for updated later
            locMap.put(currLoc.public_code, loc_view);

            // Cache background thread
            locationArray.add(currLocLive);

            // Set observer on LiveData so UI only updates when there is a change
            currLocLive.observe(this, this::updateFriendLocations);
        }
    }

    // Calculate radius adjustment for each friend and returns it as a map
    public Map<String, Integer> calculateRadiusDiff()
    {
        // Set offsets to 0 initially
        Map<String, Integer> radiusDiffList = new HashMap<>();
        for(int i = 0; i<locationArray.size(); i++)
        {
            LiveData<Location> currLocLive = locationArray.get(i);
            Location currLoc = currLocLive.getValue();
            radiusDiffList.put(currLoc.public_code, 0);
        }

        for(int i = 0; i<locationArray.size(); i++)
        {
            // Get location data for current friend
            LiveData<Location> currLocLive = locationArray.get(i);
            Location currLoc = currLocLive.getValue();

            String key = currLoc.public_code;

            // Skip locations that already stack with another location
            if(radiusDiffList.get(key) != 0)
            {
                continue;
            }

            // Get information for current location
            float currLocDegree = getDegree(currLoc);
            double currLocDistance = getDistance(currLoc);
            int currZoomLevel = zoomLevel(currLocDistance);

            for(int x = i+1; x<locationArray.size(); x++)
            {
                // Skip locations that already stack with another location
                if(radiusDiffList.get(key) != 0)
                {
                    continue;
                }

                LiveData<Location> tempCurrLocLive = locationArray.get(x);
                Location tempCurrLoc = tempCurrLocLive.getValue();
                String tempKey = tempCurrLoc.public_code;

                // Get location data for friend being compared to
                float tempCurrLocDegree = getDegree(tempCurrLoc);
                double tempCurrLocDistance = getDistance(tempCurrLoc);
                int tempCurrZoomLevel = zoomLevel(tempCurrLocDistance);

                // If labels are near enough, offset them
                if(tempCurrZoomLevel == currZoomLevel && Math.abs(tempCurrLocDegree - currLocDegree) <= 12 )
                {
                    radiusDiffList.replace(key, -30);
                    radiusDiffList.replace(tempKey, 30);

                    break;
                }
            }
        }
        return radiusDiffList;
    }

    // Calculate which zoom level the location is on
    private int zoomLevel(double distance)
    {
        if(distance >= 0 && distance < 1)
        {
            return 1;
        }
        else if(distance >= 1 && distance < 10)
        {
            return 2;
        }
        else if(distance >= 10 && distance < 500)
        {
            return 3;
        }

        else
        {
            return 4;
        }
    }

    // Get degree at which label should be displayed
    private float getDegree(Location location){
        // Coordinates of current saved location
        double locLat = location.latitude;
        double locLong = location.longitude;

        // Degree if phone facing north
        float initDegree = DegreeCalculator.degreeBetweenCoordinates(latitude, longitude, locLat, locLong);

        // Degree for current phone direction
        float rotatedDegree = DegreeCalculator.rotatingToPhoneOrientation(initDegree, (float) currOrientation);
        Log.i("DEGREE", location.public_code + ": " + rotatedDegree);
        return rotatedDegree;
    }

    // Calculate the distance at which label should be displayed
    private double getDistance(Location location){
        // Coordinates of current saved location
        double locLat = location.latitude;
        double locLong = location.longitude;

        // Degree if phone facing north
        double distance = DistanceCalculator.distanceBetweenCoordinates(latitude, longitude, locLat, locLong);
        return distance;
    }

    // Update orientation locally
    private void updateOrientation(Float orientation) {
        currOrientation = Math.toDegrees(orientation);
        Log.i("CURR ORIENTATION", currOrientation + "");
    }

    // Update a single friend's location
    private void updateFriendLocations(Location location) {
        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(() -> calculateRadiusDiff());
        try {
            Map<String, Integer> radiusDiffList = future.get();
            Log.d("CompassActivity", "updateAllFriendLocations() called");
            // Update circle in the given angle
            View newView = DisplayHelper.updateLocation(CompassActivity.this, locMap.get(location.public_code), radius-64, getDegree(location), getDistance(location), zoomLevel, location.label, radiusDiffList.get(location.public_code));
            locMap.put(location.public_code, newView);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    These are for  testing only =============================================================
     */
    private void onOrientationChanged(Float orientation) {
        currOrientation = (float)Math.toDegrees(orientation);
    }

    public void reobserveOrientation() {
        LiveData<Float> orientationData = OrientationService.singleton(this).getOrientation();
        orientationData.observe(this, this::onOrientationChanged);
    }

    public float getCurrOrientation(){
        return (float) currOrientation;
    }

    public void reobserveLocation() {
        var locationData = LocationService.singleton(this).getLocation();
        locationData.observe(this, this::onLocationChanged);
    }

    public void reobserveGPSFix() {
        var locationData = LocationService.singleton(this).getGPSFix();
        locationData.observe(this, this::updateGPSStatus);
    }

    private void onGPSFixChanged(Boolean aBoolean) {
        hasGPSFix = aBoolean;
    }

    private void onLocationChanged(Pair<Double, Double> loc) {

        latitude = loc.first.floatValue();
        longitude = loc.second.floatValue();
        Log.i("Tag", "USER LOCATION " + latitude + " " + longitude);
    }
    public float getLatitude(){
        return (float) latitude;
    }
    public float getLongitude(){
        return (float) longitude;
    }

    public void onAddFriendsButtonClicked(View view) {
        Intent intent = new Intent(this, AddFriendsActivity.class);
        startActivity(intent);
    }
}