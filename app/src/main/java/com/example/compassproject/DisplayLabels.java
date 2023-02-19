package com.example.compassproject;

import static android.content.Context.MODE_PRIVATE;

import android.view.View;

public class DisplayLabels
{
    public static void displayPopUp(CompassActivity activity, CircleView loc_view)
    {

        loc_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SavedLocations savedLocations = new SavedLocations(activity.getSharedPreferences("LocationData", MODE_PRIVATE));
                float latitude = savedLocations.getLatitude(loc_view.getIndex());
                float longitude = savedLocations.getLongitude(loc_view.getIndex());
                String label = savedLocations.getLabel(loc_view.getIndex());
                Utilities.showPopup(activity, label, "" + latitude + ", " + longitude);

            }
        });
    }
}
