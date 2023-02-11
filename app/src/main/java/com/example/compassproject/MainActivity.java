package com.example.compassproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private LocationService locationService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //request permission from user at runtime to use location
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }

        locationService = LocationService.singleton(this);
        LiveData<Pair<Double,Double>> loc = locationService.getLocation();

        //storing location on shared preferences


        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        loc.observe(this, location-> {

            editor.putFloat("Latitude", Double.valueOf(location.first).floatValue());
            editor.putFloat("Longitude", Double.valueOf(location.second).floatValue());
            editor.apply();
            //check if location is correct
            //System.out.println(location.first);
            //System.out.println(location.second);

            //check if stored location is correct
            SharedPreferences pref = getPreferences(MODE_PRIVATE);
            System.out.println("Latitude: " + pref.getFloat("Latitude", 100));
            System.out.println("Longitude: " + pref.getFloat("Longitude", 100));

        });



    }

    public void onOpenCompassClick(View view) {
        Intent intent = new Intent(this, CompassActivity.class);
        startActivity(intent);
    }

    public void onNewLocationClick(View view) {
        Intent intent = new Intent(this, CompassActivity.class);
        startActivity(intent);
    }
}