package com.example.compassproject;

import android.content.SharedPreferences;

public class SavedLocations {
    public static void saveLocation(String coords, String label, SharedPreferences preferences){
        SharedPreferences.Editor editor = preferences.edit();

        int numLocs = preferences.getInt("numLocs", 0) + 1;
        editor.putInt("numLocs", numLocs);
        editor.putFloat("latitude" + numLocs, Float.parseFloat(coords.split(",")[0]));
        editor.putFloat("longitude" + numLocs, Float.parseFloat(coords.split(",")[1]));
        editor.putString("label" + numLocs, label);
        editor.apply();
    }
}
