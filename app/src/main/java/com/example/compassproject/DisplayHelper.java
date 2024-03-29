package com.example.compassproject;


import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
public class DisplayHelper {

    //final static int LOCATION_VIEW_BASE_ID = 1;
    public static View displaySingleLocation(CompassActivity activity, int loc_id, int radius, float degrees, double distance, int zoomLevel, String name, int diff) {
        if (zoomLevel == 1) {
            return displaySingleLocationZoomOne(activity, loc_id, radius, degrees, distance, name, diff);
        }

        if (zoomLevel == 2) {
            return displaySingleLocationZoomTwo(activity, loc_id, radius, degrees, distance, name, diff);
        }

        if (zoomLevel == 3) {
            return displaySingleLocationZoomThree(activity, loc_id, radius, degrees, distance, name, diff);
        }

        if (zoomLevel == 4) {
            return displaySingleLocationZoomFour(activity, loc_id, radius, degrees, distance, name, diff);
        }

        return null;
    }

    public static View displaySingleLocationZoomOne(CompassActivity activity, int loc_id, int radius, float degrees, double distance, String name, int diff) {
        ConstraintLayout cl = activity.findViewById(R.id.compass_cl);
        ConstraintSet cs = new ConstraintSet();

        if (distance < 1) {
            TextView friendName = new TextView(activity);
            friendName.setText(name);

            cl.addView(friendName, loc_id);
            friendName.setId(View.generateViewId());
            cs.clone(cl);
            cs.constrainCircle(friendName.getId(), R.id.compass_face, (int) (radius/2 + diff), degrees);
            cs.applyTo(cl);

            return friendName;
        }

        //display circle at edge of compass
        else {
            CircleView loc_view = new CircleView(activity);
            cl.addView(loc_view, loc_id);
            loc_view.setId(View.generateViewId());
            cs.clone(cl);
            cs.constrainCircle(loc_view.getId(), R.id.compass_face, radius, degrees);
            cs.applyTo(cl);
            return loc_view;
        }
    }

    public static View displaySingleLocationZoomTwo(CompassActivity activity, int loc_id, int radius, float degrees, double distance, String name, int diff) {
        ConstraintLayout cl = activity.findViewById(R.id.compass_cl);
        ConstraintSet cs = new ConstraintSet();

        if (distance <= 1) {
            TextView friendName = new TextView(activity);
            friendName.setText(name);

            cl.addView(friendName, loc_id);
            friendName.setId(View.generateViewId());
            cs.clone(cl);
            cs.constrainCircle(friendName.getId(), R.id.compass_face, (int) radius/4 + diff, degrees);
            cs.applyTo(cl);

            return friendName;
        } else if (distance < 10) {
            TextView friendName = new TextView(activity);
            friendName.setText(name);

            cl.addView(friendName, loc_id);
            friendName.setId(View.generateViewId());
            cs.clone(cl);
            cs.constrainCircle(friendName.getId(), R.id.compass_face, (int) (radius * 3/4) + diff, degrees);
            cs.applyTo(cl);

            return friendName;
        } else {
            CircleView loc_view = new CircleView(activity);
            cl.addView(loc_view, loc_id);
            loc_view.setId(View.generateViewId());
            cs.clone(cl);
            cs.constrainCircle(loc_view.getId(), R.id.compass_face, radius, degrees);
            cs.applyTo(cl);
            return loc_view;
        }
    }

    public static View displaySingleLocationZoomThree(CompassActivity activity, int loc_id, int radius, float degrees, double distance, String name, int diff) {
        ConstraintLayout cl = (ConstraintLayout) activity.findViewById(R.id.compass_cl);
        ConstraintSet cs = new ConstraintSet();
        double r1 = radius / 3;
        double r2 = radius * 2 / 3;

        if (distance <= 1) {
            TextView friendName = new TextView(activity);
            friendName.setText(name);

            cl.addView(friendName, loc_id);
            friendName.setId(View.generateViewId());
            cs.clone(cl);
            cs.constrainCircle(friendName.getId(), R.id.compass_face, (int) radius/6 + diff, degrees);
            cs.applyTo(cl);

            return friendName;
        } else if (distance <= 10) {
            TextView friendName = new TextView(activity);
            friendName.setText(name);

            cl.addView(friendName, loc_id);
            friendName.setId(View.generateViewId());
            cs.clone(cl);
            cs.constrainCircle(friendName.getId(), R.id.compass_face, (int) (radius*3/6) + diff, degrees);
            cs.applyTo(cl);

            return friendName;
        } else if (distance < 500) {
            TextView friendName = new TextView(activity);
            friendName.setText(name);

            cl.addView(friendName, loc_id);
            friendName.setId(View.generateViewId());
            cs.clone(cl);
            cs.constrainCircle(friendName.getId(), R.id.compass_face, (int) (radius*5/6) + diff, degrees);
            cs.applyTo(cl);


            return friendName;
        } else {
            CircleView loc_view = new CircleView(activity);
            cl.addView(loc_view, loc_id);
            loc_view.setId(View.generateViewId());
            cs.clone(cl);
            cs.constrainCircle(loc_view.getId(), R.id.compass_face, radius, degrees);
            cs.applyTo(cl);
            return loc_view;
        }
    }

    public static View displaySingleLocationZoomFour(CompassActivity activity, int loc_id, int radius, float degrees, double distance, String name, int diff) {
        ConstraintLayout cl = (ConstraintLayout) activity.findViewById(R.id.compass_cl);
        ConstraintSet cs = new ConstraintSet();

        if (distance <= 1) {
            double maxDistance = radius / 4;
            TextView friendName = new TextView(activity);
            friendName.setText(name);

            cl.addView(friendName, loc_id);
            friendName.setId(View.generateViewId());
            cs.clone(cl);
            cs.constrainCircle(friendName.getId(), R.id.compass_face, (int) radius/8 + diff, degrees);
            cs.applyTo(cl);

            return friendName;
        } else if (distance <= 10) {
            TextView friendName = new TextView(activity);
            friendName.setText(name);

            cl.addView(friendName, loc_id);
            friendName.setId(View.generateViewId());
            cs.clone(cl);
            cs.constrainCircle(friendName.getId(), R.id.compass_face, (int) (radius*3/8) + diff, degrees);
            cs.applyTo(cl);

            return friendName;
        } else if (distance <= 500) {
            TextView friendName = new TextView(activity);
            friendName.setText(name);

            cl.addView(friendName, loc_id);
            friendName.setId(View.generateViewId());
            cs.clone(cl);
            cs.constrainCircle(friendName.getId(), R.id.compass_face, (int) (radius*5/8) + diff, degrees);
            cs.applyTo(cl);

            return friendName;
        } else {
            TextView friendName = new TextView(activity);
            friendName.setText(name);

            cl.addView(friendName, loc_id);
            friendName.setId(View.generateViewId());
            cs.clone(cl);
            cs.constrainCircle(friendName.getId(), R.id.compass_face, (int) (radius*7/8) + diff, degrees);
            cs.applyTo(cl);

            return friendName;
        }
    }

    public static View updateLocation(CompassActivity activity, View loc_view, int radius, float degrees, double distance, int zoomLevel, String friendName, int diff) {
        double maxDistance = 0;
        if (zoomLevel == 1) {
            return updateLocationOne(activity, loc_view, radius, degrees, distance, friendName, diff);
        }

        if (zoomLevel == 2) {
            return updateLocationTwo(activity, loc_view, radius, degrees, distance, friendName, diff);
        }

        if (zoomLevel == 3) {
            return updateLocationThree(activity, loc_view, radius, degrees, distance, friendName, diff);
        }

        if (zoomLevel == 4) {
            return updateLocationFour(activity, loc_view, radius, degrees, distance, friendName, diff);
        }

        return null;
    }

    public static View updateLocationOne(CompassActivity activity, View loc_view, int radius, float degrees, double distance, String friendName, int diff) {
        ConstraintLayout cl = (ConstraintLayout) activity.findViewById(R.id.compass_cl);
        ConstraintSet cs = new ConstraintSet();
        cl.removeView(loc_view);
        return displaySingleLocation(activity, 1, radius, degrees, distance, 1, friendName, diff);

    }

    public static View updateLocationTwo(CompassActivity activity, View loc_view, int radius, float degrees, double distance, String friendName, int diff) {
        ConstraintLayout cl = (ConstraintLayout) activity.findViewById(R.id.compass_cl);
        ConstraintSet cs = new ConstraintSet();
        cl.removeView(loc_view);
        return displaySingleLocation(activity, 1, radius, degrees, distance, 2, friendName, diff);

    }
    public static View updateLocationThree(CompassActivity activity, View loc_view, int radius, float degrees, double distance, String friendName, int diff) {
        ConstraintLayout cl = (ConstraintLayout) activity.findViewById(R.id.compass_cl);
        ConstraintSet cs = new ConstraintSet();
        cl.removeView(loc_view);
        return displaySingleLocation(activity, 1, radius, degrees, distance, 3, friendName, diff);

    }

    public static View updateLocationFour(CompassActivity activity, View loc_view, int radius, float degrees, double distance, String friendName, int diff) {
        ConstraintLayout cl = (ConstraintLayout) activity.findViewById(R.id.compass_cl);
        cl.removeView(loc_view);
        return displaySingleLocation(activity, 1, radius, degrees, distance, 4, friendName, diff);
    }

}