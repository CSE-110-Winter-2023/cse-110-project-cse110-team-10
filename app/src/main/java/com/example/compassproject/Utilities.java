/*
Utilities holds methods which are shared between various classes and activities
 */
package com.example.compassproject;

import android.app.Activity;
import android.app.AlertDialog;

/**
 * This class is utilities class where we has helper functions
 * to show pops-up, format elapsed time
 * Functions    showPopup
 *              formatElapsedTime
 */
public class Utilities {
    /**
     * This method is used to display the pop up with info
     * on screen
     * @param activity
     * @param title
     * @param message
     */
    public static void showPopup(Activity activity, String title, String message){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);

        alertBuilder
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok",(dialog, id) -> {

                    dialog.cancel();
                })
                .setCancelable(true);

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    /**
     * This method is used to format the elapsed time to
     * hours, minutes properly.
     * @param time
     * @return hours, minutes
     */
    public static String formatElapsedTime(long time) {
        if(time > 3600000){
            return (time)/3600000 + "h";
        }
        if(time > 60000){
            return (time)/60000 + "m";
        }
        else{
            return "";
        }
    }
}

