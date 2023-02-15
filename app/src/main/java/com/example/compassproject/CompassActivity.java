package com.example.compassproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CompassActivity extends AppCompatActivity {
    static int radius;
    SavedLocations savedLocations;
    private ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();
    private Future<Void> future;


    /*
     * TODO: Update locations and angles for compass_N, compass_E, compass_S, compass_W
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        storeUserLoc();
        savedLocations = new SavedLocations(getSharedPreferences("LocationData", MODE_PRIVATE));
        SharedPreferences preferences = getSharedPreferences("Location", Context.MODE_PRIVATE);

        final ImageView compass = (ImageView) findViewById(R.id.compass_face);
        ViewTreeObserver observer = compass.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                TextView north = (TextView) findViewById(R.id.compass_N);
                TextView east = (TextView) findViewById(R.id.compass_E);
                TextView south = (TextView) findViewById(R.id.compass_S);
                TextView west = (TextView) findViewById(R.id.compass_W);

                int rad = compass.getHeight() / 2;
                radius = rad;

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
                 */;
                float userLat = SavedUserLocation.getUserLatitude(preferences);
                float userLong = SavedUserLocation.getUserLongitude(preferences);
                int numLocations = savedLocations.getNumLocations();

                for(int i = 0; i < numLocations; i++){
                    float locLat = savedLocations.getLatitude(i);
                    float locLong = savedLocations.getLongitude(i);
                    float degree = DegreeCalculator.degreeBetweenCoordinates(userLat, userLong, locLat, locLong);
                    DisplayHelper.displaySingleLocation(CompassActivity.this, 1, rad-64, degree);
                }
                compass.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        storeUserLoc();
    }

    private void storeUserLoc() {
        SavedUserLocation.saveUserLoc(this, LocationService.singleton(this), getPreferences(MODE_PRIVATE));
    }

    public void onAddLocationClicked(View view) {
        if (this.future != null) {
            this.future.cancel(true);
        }
        Intent intent = new Intent(this, NewLocationActivity.class);
        startActivity(intent);
    }

    public void rotateThreadHandler(int rotationDegrees) {
        this.future = backgroundThreadExecutor.submit(() -> {
            do {
                //update angles for N,E,S,W letters
                TextView north = (TextView) findViewById(R.id.compass_N);
                TextView east = (TextView) findViewById(R.id.compass_E);
                TextView south = (TextView) findViewById(R.id.compass_S);
                TextView west = (TextView) findViewById(R.id.compass_W);

                ConstraintLayout.LayoutParams north_lp = (ConstraintLayout.LayoutParams) north.getLayoutParams();
                north_lp.circleAngle = rotationDegrees;

                ConstraintLayout.LayoutParams east_lp = (ConstraintLayout.LayoutParams) east.getLayoutParams();
                east_lp.circleAngle = 90 + rotationDegrees;

                ConstraintLayout.LayoutParams south_lp = (ConstraintLayout.LayoutParams) south.getLayoutParams();
                south_lp.circleAngle = 180 + rotationDegrees;

                ConstraintLayout.LayoutParams west_lp = (ConstraintLayout.LayoutParams) west.getLayoutParams();
                west_lp.circleAngle =  270 + rotationDegrees;


                runOnUiThread(() -> {
                    north.setLayoutParams(north_lp);
                    east.setLayoutParams(east_lp);
                    south.setLayoutParams(south_lp);
                    west.setLayoutParams(west_lp);
                });

            } while (true);
        });

    }
}