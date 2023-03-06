// ONLY FOR SKETCHING OUT REFACTORING

package com.example.compassproject;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.compassproject.ViewModel.CompassViewModel;
import com.example.compassproject.model.Location;
import com.example.compassproject.model.LocationRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CompassActivity2 extends AppCompatActivity {
    private HashMap<String, CircleView> locMap = new HashMap<>();
    private double currOrientation;
    private double latitude;
    private double longitude;
    private int radius;
    private ImageView compass;

    CompassViewModel viewModel;

    ArrayList<LiveData<Location>> locationArray;

    ConstraintLayout.LayoutParams north_lp, south_lp, east_lp, west_lp;
    TextView north, south, east, west;

    LocationRepository locRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);


        viewModel = setupViewModel();
        // Create location and orientation services
        LocationService ls = LocationService.singleton(this);
        OrientationService os = OrientationService.singleton(this);

        // Make sure compass has been set up on UI
        compass = (ImageView) findViewById(R.id.compass_face);
        ViewTreeObserver observer = compass.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setupUI();

                // Continuously update location data to local fields
                ls.getLocation().observe(CompassActivity2.this, location -> {
                    updateCoordinates(location);
                    updateAllFriendLocations();

                });

                // Continuously update orientation data to local fields
                os.getOrientation().observe(CompassActivity2.this, orientation -> {
                    updateOrientation(orientation);
                    updateCardinalAxisLabels();
                    updateAllFriendLocations();
                });
            }
        });
    }

    public void updateAllFriendLocations(){
        // showing red dots which is saved user locations
        for(int i = 0; i < locationArray.size(); i++){
            LiveData<Location> currLocLive = locationArray.get(i);
            Location currLoc = currLocLive.getValue();

            updateFriendLocations(currLoc);
        }
    }
    // Update coordinates both locally and remotely
    private void updateCoordinates(Pair<Double, Double> location) {
        updateCoordinatesLocal(location);
        updateCoordinatesRemote(location);
    }

    private void updateCoordinatesLocal(Pair<Double, Double> location){
        latitude = location.first;
        longitude = location.second;
    }

    private void updateCoordinatesRemote(Pair<Double, Double> location){
        viewModel.updateCoordinatesRemote(location);
    }
    private CompassViewModel setupViewModel() {
        return new ViewModelProvider(this).get(CompassViewModel.class);
    }

    private void setupUI() {
        setupCardinalAxisLabels();
        setupFriendLocations();
    }

    private void updateCardinalAxisLabels(){
        north_lp.circleAngle = (float) -currOrientation;
        east_lp.circleAngle = (float) (90 - currOrientation);
        south_lp.circleAngle = (float) (180 - currOrientation);
        west_lp.circleAngle = (float) (270 - currOrientation);

        // Place cardinal direction labels in correct location
        north.setLayoutParams(north_lp);
        east.setLayoutParams(east_lp);
        south.setLayoutParams(south_lp);
        west.setLayoutParams(west_lp);
    }

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

    // Calculate radius
    private void setRadius(){
        radius = compass.getHeight() / 2;
    }

    private void setupFriendLocations(){
        // TODO: Use Friends Database to get list of UIDs
        List<String> friendList = viewModel.getAllFriendUIDs(); // Move to ViewModel
        int numFriends = friendList.size();

        // showing red dots which is saved user locations
        for(int i = 0; i < numFriends; i++){
            LiveData<Location> currLocLive = viewModel.getLiveLocation(friendList.get(i)); // Move to ViewModel
            Location currLoc = currLocLive.getValue();

            // Create circle in the given angle
            CircleView loc_view = DisplayHelper.displaySingleLocation(CompassActivity.this, 1, radius-64, getDegree(currLoc));
            locMap.put(currLoc.public_code, loc_view);

            // caching background thread
            locationArray.add(currLocLive);

            //Will be replaced with new code for handling CircleView vs Label
            loc_view.setIndex(i);
            DisplayLabels.displayPopUp(CompassActivity.this,loc_view);

            // Set observer on LiveData so UI only updates when there is a change
            currLocLive.observe(this, this::updateFriendLocations);
        }
    }

    private double getDegree(Location location){
        // Coordinates of current saved location
        double locLat = location.latitude;
        double locLong = location.longitude;

        // Degree if phone facing north
        float initDegree = DegreeCalculator.degreeBetweenCoordinates(latitude, longitude, locLat, locLong);

        // Degree for current phone direction
        return DegreeCalculator.rotatingToPhoneOrientation(initDegree, (float) currOrientation);
    }

    private void updateOrientation(Float orientation) {
        currOrientation = orientation;
    }

    private void updateFriendLocations(Location location) {
        // Update circle in the given angle
        DisplayHelper.updateLocation(CompassActivity.this, locMap.get(location.public_code), radius-64, getDegree(location));
    }
}