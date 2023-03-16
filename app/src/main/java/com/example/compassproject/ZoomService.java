package com.example.compassproject;

import android.content.Context;
import android.content.SharedPreferences;

public class ZoomService {
    private static SharedPreferences preferences;
    private static int zoomLevel;
    public ZoomService(Context context) {
        // Initialize shared preferences
        preferences = context.getSharedPreferences("ZoomData", Context.MODE_PRIVATE);
        // Load the zoom level from shared preferences
        zoomLevel = preferences.getInt("zoom_level", 2);
    }

    public int getZoomLevel() {
        zoomLevel = preferences.getInt("zoom_level", 2);
        return zoomLevel;
    }

    public static void zoomIn(){
        SharedPreferences.Editor editor = preferences.edit();
        switch(zoomLevel) {
            case 1:
            case 2:
                editor.putInt("zoom_level", 1);
                editor.apply();
                break;
            case 3:
                editor.putInt("zoom_level", 2);
                editor.apply();
                break;
            case 4:
                editor.putInt("zoom_level", 3);
                editor.apply();
                break;
        }
    }

    public static void zoomOut() {
        SharedPreferences.Editor editor = preferences.edit();
        switch(zoomLevel) {
            case 1:
                editor.putInt("zoom_level", 2);
                editor.apply();
                break;
            case 2:
                editor.putInt("zoom_level", 3);
                editor.apply();
                break;
            case 3:
            case 4:
                editor.putInt("zoom_level", 4);
                editor.apply();
                break;
        }
    }

}
