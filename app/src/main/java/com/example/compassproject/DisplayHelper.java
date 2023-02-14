package com.example.compassproject;

import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class DisplayHelper {

    final static int LOCATION_VIEW_BASE_ID = 1;

    public static void displaySingleLocation(CompassActivity activity, int loc_id, int radius, float degrees) {

        int loc_view_id = LOCATION_VIEW_BASE_ID + loc_id;

        ConstraintLayout cl = (ConstraintLayout) activity.findViewById(R.id.compass_cl);
        ConstraintSet cs = new ConstraintSet();

        CircleView loc_view = new CircleView(activity);
        cl.addView(loc_view, loc_id);
        loc_view.setId(View.generateViewId());

        cs.clone(cl);

        cs.constrainCircle(loc_view.getId(), R.id.compass_face, radius, degrees);

        cs.applyTo(cl);
    }
}