package com.example.compassproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ZoomService {
    private static SharedPreferences preferences;
    private static int zoomRadius;
    public ZoomService(Context context) {
        // Initialize shared preferences
        preferences = context.getSharedPreferences("ZoomData", Context.MODE_PRIVATE);
        // Load the zoom level from shared preferences
        zoomRadius = preferences.getInt("zoom_radius", 10);
    }

    public int getZoomLevel() {
        zoomRadius = preferences.getInt("zoom_radius", 10);
        return zoomRadius;
    }

    public static void zoomIn(){
        SharedPreferences.Editor editor = preferences.edit();
        switch(zoomRadius) {
            case 1:
            case 10:
                editor.putInt("zoom_radius", 1);
                editor.apply();
                break;
            case 500:
                editor.putInt("zoom_radius", 10);
                editor.apply();
                break;
                //TODO: Clarify outer zoom level
            case 1000:
                editor.putInt("zoom_radius", 500);
                editor.apply();
                break;
        }
    }

    public static void zoomOut() {
        SharedPreferences.Editor editor = preferences.edit();
        switch(zoomRadius) {
            case 1:
                editor.putInt("zoom_radius", 10);
                editor.apply();
                break;
            case 10:
                editor.putInt("zoom_radius", 500);
                editor.apply();
                break;
            case 500:
                //TODO: Clarify outer zoom level
            case 1000:
                editor.putInt("zoom_radius", 1000);
                editor.apply();
                break;
        }
    }

}
