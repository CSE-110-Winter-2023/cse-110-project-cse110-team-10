package com.example.compassproject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.location.LocationListener;
import android.util.Pair;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

public class SavedUserLocation
{
    static float latitude;
    static float longitude;

    public static void saveUserLoc(LifecycleOwner owner, LocationService locationService)
    {
        LiveData<Pair<Double,Double>> loc = locationService.getLocation();

        loc.observe(owner, location-> {
            latitude = location.first.floatValue();
            longitude = location.second.floatValue();
        });
    }

    public static float getUserLatitude()
    {
        return latitude;
    }

    public static float getUserLongitude()
    {
        return longitude;
    }
}
