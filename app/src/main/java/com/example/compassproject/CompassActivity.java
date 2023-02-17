package com.example.compassproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CompassActivity extends AppCompatActivity {
    static int radius;
    SavedLocations savedLocations;

    private ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();
    private Future<Void> future;

    private float currOrientation;
    private float latitude;
    private float longitude;

    /*
     * TODO: Update locations and angles for compass_N, compass_E, compass_S, compass_W
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        // Get coordinates and labels entered by user
        savedLocations = new SavedLocations(getSharedPreferences("LocationData", MODE_PRIVATE));

        // Create location and orientation services
        LocationService ls = LocationService.singleton(this);
        OrientationService os = OrientationService.singleton(this);

        // Continuously update location data to local fields
        ls.getLocation().observe(this, location -> {
            latitude = location.first.floatValue();
            longitude = location.second.floatValue();
        });

        // Continuously update orientation data to local fields
        os.getOrientation().observe(this, orientation -> {
            currOrientation = (float) Math.toDegrees(orientation);
        });

        // Make sure compass has been set up on UI
        final ImageView compass = (ImageView) findViewById(R.id.compass_face);
        ViewTreeObserver observer = compass.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Access UI elements for cardinal directions
                TextView north = (TextView) findViewById(R.id.compass_N);
                TextView east = (TextView) findViewById(R.id.compass_E);
                TextView south = (TextView) findViewById(R.id.compass_S);
                TextView west = (TextView) findViewById(R.id.compass_W);

                // Calculate radius
                int rad = compass.getHeight() / 2;
                radius = rad;

                // Placing cardinal direction labels in correct initial location
                ConstraintLayout.LayoutParams north_lp = (ConstraintLayout.LayoutParams) north.getLayoutParams();
                north_lp.circleRadius = rad;
                north.setLayoutParams(north_lp);

                ConstraintLayout.LayoutParams east_lp = (ConstraintLayout.LayoutParams) east.getLayoutParams();
                east_lp.circleRadius = rad;
                east.setLayoutParams(east_lp);

                ConstraintLayout.LayoutParams south_lp = (ConstraintLayout.LayoutParams) south.getLayoutParams();
                south_lp.circleRadius = rad;
                south.setLayoutParams(south_lp);

                ConstraintLayout.LayoutParams west_lp = (ConstraintLayout.LayoutParams) west.getLayoutParams();
                west_lp.circleRadius = rad;
                west.setLayoutParams(west_lp);

                //TODO: loop through all locations w correct degrees
                /* degrees ==> SavedLocation.getDegrees(loc_id)
                 */

                // Run background thread to update UI with location and orientation every 250 ms
                CompassActivity.this.future = backgroundThreadExecutor.submit(() -> {
                    do{
                        // Make final copies of most updated location and orientation data
                        final float latitudeCopy = latitude;
                        final float longitudeCopy = longitude;
                        final float orientationCopy = currOrientation;

                        // Make final copy of number of locations added
                        int numLocations = savedLocations.getNumLocations();
                        final int numLocationsCopy = numLocations;

                        runOnUiThread(() -> {
                            // Calculate new angle of cardinal direction labels
                            north_lp.circleAngle = -orientationCopy;
                            east_lp.circleAngle = 90 - orientationCopy;
                            south_lp.circleAngle = 180 - orientationCopy;
                            west_lp.circleAngle = 270 - orientationCopy;

                            // Place cardinal direction labels in correct location
                            north.setLayoutParams(north_lp);
                            east.setLayoutParams(east_lp);
                            south.setLayoutParams(south_lp);
                            west.setLayoutParams(west_lp);

                            // Create circles representing relative locations
                            for(int i = 0; i < numLocationsCopy; i++){
                                // Coordinates of current saved location
                                float locLat = savedLocations.getLatitude(i);
                                float locLong = savedLocations.getLongitude(i);

                                // Degree if phone facing north
                                float initDegree = DegreeCalculator.degreeBetweenCoordinates(latitudeCopy, longitudeCopy, locLat, locLong);

                                // Degree for current phone direction
                                float degree = DegreeCalculator.rotatingToPhoneOrientation(initDegree, orientationCopy);

                                // Create circle in the given angle
                                DisplayHelper.displaySingleLocation(CompassActivity.this, 1, rad-64, degree);
                                // TODO: Update instead of create new circle
                            }
                        });

                        Log.d("Location", "(" + latitudeCopy + ", " + longitudeCopy + ")");
                        Log.d("Orientation", "" + orientationCopy);

                        Thread.sleep(250);
                    } while(true);
                });

                compass.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    public void onAddLocationClicked(View view) {
        Intent intent = new Intent(this, NewLocationActivity.class);
        startActivity(intent);
    }

}