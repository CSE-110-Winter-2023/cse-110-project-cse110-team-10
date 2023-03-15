package com.example.compassproject;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import android.content.SharedPreferences;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class DisplayHelper {

    //final static int LOCATION_VIEW_BASE_ID = 1;

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

            ViewGroup.LayoutParams params = friendName.getLayoutParams();

            // Set labels to truncate
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            friendName.setLayoutParams(params);
            friendName.setEllipsize(TextUtils.TruncateAt.END);
            friendName.setSingleLine(true);

            friendName.setText(name);
            cl.addView(friendName, loc_id);

            friendName.setId(View.generateViewId());
            cs.clone(cl);
            cs.constrainCircle(friendName.getId(), R.id.compass_face, (int) circleRad, degrees);
            cs.applyTo(cl);
            return friendName;
        }
    }
    public static View updateLocation(CompassActivity activity, View loc_view, int radius, float degrees, double distance, double maxDistance, String friendName){
        ConstraintLayout cl = (ConstraintLayout) activity.findViewById(R.id.compass_cl);
        ConstraintSet cs = new ConstraintSet();
        double distancePercentOfMax = distance / maxDistance;
        double circleRad = distancePercentOfMax * radius;
        if(circleRad > radius){
            //previously inside compass, now on edge. text --> circle
            if(loc_view instanceof TextView) {
                cl.removeView(loc_view);
                return displaySingleLocation(activity, 1, radius, degrees, distance, maxDistance, friendName);
            }

            //stays in circle as a TextView
            else {
                cs.clone(cl);
                cs.constrainCircle(loc_view.getId(), R.id.compass_face, radius, degrees);
                cs.applyTo(cl);
                return loc_view;
            }
        }

        else{
            if(loc_view instanceof CircleView) {
                cl.removeView(loc_view);
                return displaySingleLocation(activity, 1, radius, degrees, distance, maxDistance, friendName);
            }

            //stays outside as a CircleView
            else {
                cs.clone(cl);
                cs.constrainCircle(loc_view.getId(), R.id.compass_face, radius, degrees);
                cs.applyTo(cl);
                return loc_view;
            }

        }
    }
}
