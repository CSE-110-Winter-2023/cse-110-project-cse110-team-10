package com.example.compassproject;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * This class is used as zoom service which used to
 * get zoom level, zoom in, zoom out features.
 * Functions    ZoomService
 *              getZoomLevel
 *              zoomIn
 *              zoomOut
 */
public class ZoomService {
    private static SharedPreferences preferences;
    private static int zoomLevel;

    /**
     * This is zoomService constructor
     * @param context
     */
    public ZoomService(Context context) {
        // Initialize shared preferences
        preferences = context.getSharedPreferences("ZoomData", Context.MODE_PRIVATE);
        // Load the zoom level from shared preferences
        zoomLevel = preferences.getInt("zoom_level", 2);
    }

    /**
     * This method is used to get zoom level
     * @return zoomLevel
     */
    public int getZoomLevel() {
        zoomLevel = preferences.getInt("zoom_level", 2);
        return zoomLevel;
    }

    /**
     * This method is used to zoom in
     * which has 3 levels to zoom in
     */
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

    /**
     * This method is used to zoom out
     */
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
