package com.example.compassproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

public class CompassActivity extends AppCompatActivity {
    static int radius;
    SavedLocations savedLocations;

    /*
     * TODO: Update locations and angles for compass_N, compass_E, compass_S, compass_W
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        storeUserLoc();
        savedLocations = new SavedLocations(getPreferences(MODE_PRIVATE));

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

                 float userLat = getUserLatitude();
                float userLong = getUserLongitude();
                int numLocations = savedLocations.getNumLocations();
                System.out.println(numLocations);

                for(int i = 0; i <= 1; i++){
                    float locLat = (float) 90.3;
                    float locLong = (float) 90.3;
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

    private float getUserLatitude() {
        return SavedUserLocation.getUserLatitude(getPreferences(MODE_PRIVATE));
    }

    private float getUserLongitude() {
        return SavedUserLocation.getUserLongitude(getPreferences(MODE_PRIVATE));
    }

    public void onAddLocationClicked(View view) {
        Intent intent = new Intent(this, NewLocationActivity.class);
        startActivity(intent);
    }

}