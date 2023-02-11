package com.example.compassproject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Pair;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

public class SavedUserLocation
{
    public static void saveUserLoc(LifecycleOwner owner, LocationService locationService, SharedPreferences preferences)
    {


        SharedPreferences.Editor editor = preferences.edit();
        LiveData<Pair<Double,Double>> loc = locationService.getLocation();

        loc.observe(owner, location-> {

            editor.putFloat("User Latitude", Double.valueOf(location.first).floatValue());
            editor.putFloat("User Longitude", Double.valueOf(location.second).floatValue());
            editor.apply();

            System.out.println(preferences.getFloat("User Latitude", 100));
            System.out.println(preferences.getFloat("User Longitude", 100));
        });
    }
}
