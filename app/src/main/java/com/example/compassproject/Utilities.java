/*
Utilities holds methods which are shared between various classes and activities
 */
package com.example.compassproject;

import android.app.Activity;
import android.app.AlertDialog;

public class Utilities {
    // Displays popup with info on screen
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

    public static String formatElapsedTime(long time) {
        return time + "";
    }
}

