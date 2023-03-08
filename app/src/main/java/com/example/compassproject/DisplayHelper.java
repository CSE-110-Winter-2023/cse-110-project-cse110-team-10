package com.example.compassproject;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

import android.content.SharedPreferences;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class DisplayHelper {

    //final static int LOCATION_VIEW_BASE_ID = 1;

    //TODO: Remove when CompassActivity is merged

    public static View displaySingleLocation(CompassActivity activity, int loc_id, int radius, float degrees, double distance, double maxDistance, String name) {
        ConstraintLayout cl = (ConstraintLayout) activity.findViewById(R.id.compass_cl);
        ConstraintSet cs = new ConstraintSet();
        double distancePercentOfMax = distance / maxDistance;
        double circleRad = distancePercentOfMax * radius;

        //display circle at edge of compass
        if(circleRad > radius){
            CircleView loc_view = new CircleView(activity);
            cl.addView(loc_view, loc_id);
            loc_view.setId(View.generateViewId());
            cs.clone(cl);
            cs.constrainCircle(loc_view.getId(), R.id.compass_face, radius, degrees);
            cs.applyTo(cl);

            return loc_view;
        }

        //display name
        else{
            TextView friendName = new TextView(activity);
            friendName.setText(name);
            cl.addView(friendName, loc_id);
            friendName.setId(View.generateViewId());
            cs.clone(cl);
            cs.constrainCircle(friendName.getId(), R.id.compass_face, (int) circleRad, degrees);
            cs.applyTo(cl);
            return friendName;
        }
    }

    public static CircleView displaySingleLocation(CompassActivity2 activity, int loc_id, int radius, float degrees, double distance, double maxDistance) {

        // int loc_view_id = LOCATION_VIEW_BASE_ID + loc_id;

        ConstraintLayout cl = (ConstraintLayout) activity.findViewById(R.id.compass_cl);
        ConstraintSet cs = new ConstraintSet();

        CircleView loc_view = new CircleView(activity);
        cl.addView(loc_view, loc_id);
        loc_view.setId(View.generateViewId());

        double distancePercentOfMax = distance / maxDistance;
        double circleRad = distancePercentOfMax * radius;
        if(circleRad > radius){
            circleRad = radius;
        }
        cs.clone(cl);

        cs.constrainCircle(loc_view.getId(), R.id.compass_face, (int) circleRad, degrees);

        cs.applyTo(cl);

        return loc_view;
    }


//TODO: Remove when CompassActivity is merged
    public static void updateLocation(CompassActivity activity, View loc_view, int radius, float degrees, double distance, double maxDistance, String friendName){
        ConstraintLayout cl = (ConstraintLayout) activity.findViewById(R.id.compass_cl);
        ConstraintSet cs = new ConstraintSet();
        double distancePercentOfMax = distance / maxDistance;
        double circleRad = distancePercentOfMax * radius;
        if(circleRad > radius){
            //previously inside compass, now on edge. text --> circle
            if(loc_view instanceof TextView) {
                cl.removeView(loc_view);
                displaySingleLocation(activity, 1, radius, degrees, distance, maxDistance, friendName);
            }

            //stays in circle as a TextView
            else {
                cs.clone(cl);
                cs.constrainCircle(loc_view.getId(), R.id.compass_face, radius, degrees);
                cs.applyTo(cl);
            }
        }

        else{
            if(loc_view instanceof CircleView) {
                cl.removeView(loc_view);
                displaySingleLocation(activity, 1, radius, degrees, distance, maxDistance, friendName);
            }

            //stays outside as a CircleView
            else {
                cs.clone(cl);
                cs.constrainCircle(loc_view.getId(), R.id.compass_face, radius, degrees);
                cs.applyTo(cl);
            }

        }
        return;
    }

    public static void updateLocation(CompassActivity2 activity, CircleView loc_view, int radius, float degrees, double distance, double maxDistance){
        ConstraintLayout cl = (ConstraintLayout) activity.findViewById(R.id.compass_cl);
        ConstraintSet cs = new ConstraintSet();
        int circleRad = (int) (distance / maxDistance * 0.5 * cl.getWidth());
        if(circleRad > radius){
            circleRad = radius;
        }

        cs.clone(cl);


        cs.constrainCircle(loc_view.getId(), R.id.compass_face, (int) circleRad, degrees);

        cs.applyTo(cl);
    }
}
