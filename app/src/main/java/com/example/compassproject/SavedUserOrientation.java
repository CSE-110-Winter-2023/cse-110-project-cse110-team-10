package com.example.compassproject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.location.LocationListener;
import android.util.Pair;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

public class SavedUserOrientation
{
    public static void saveUserOrientation(LifecycleOwner owner, OrientationService orientationService, SharedPreferences preferences)
    {
        SharedPreferences.Editor editor = preferences.edit();
        LiveData<Float> loc = orientationService.getOrientation();

        loc.observe(owner, orientation-> {

            editor.putFloat("User Orientation", (float) Math.toDegrees(orientation.floatValue()));
            editor.apply();

        });
    }

    public static float getUserOrientation(SharedPreferences preferences)
    {
        return preferences.getFloat("User Orientation", 0);
    }
}
