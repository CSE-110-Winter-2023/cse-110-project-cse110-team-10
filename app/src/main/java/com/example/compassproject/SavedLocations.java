package com.example.compassproject;

import android.content.SharedPreferences;
public class SavedLocations {
    SharedPreferences preferences;
    public SavedLocations(SharedPreferences preferences){
        this.preferences = preferences;
    }

    public void saveLocation(String coords, String label){
        SharedPreferences.Editor editor = preferences.edit();
        int numLocs = preferences.getInt("numLocs", 0);
        editor.putInt("numLocs", numLocs + 1);
        editor.putFloat("latitude" + numLocs, Float.parseFloat(coords.split(",")[0]));
        editor.putFloat("longitude" + numLocs, Float.parseFloat(coords.split(",")[1]));
        editor.putString("label" + numLocs, label);
        editor.apply();
    }

    public int getNumLocations(){
        return preferences.getInt("numLocs", 0);
    }

    public float getLatitude(int index){
        return preferences.getFloat("latitude" + index, -100);
    }

    public float getLongitude(int index){
        return preferences.getFloat("longitude" + index, -200);
    }

    public String getLabel(int index){
        return preferences.getString("label" + index, "");
    }
}
