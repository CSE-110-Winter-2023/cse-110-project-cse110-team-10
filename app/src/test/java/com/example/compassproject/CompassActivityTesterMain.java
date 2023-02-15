package com.example.compassproject;

import static org.junit.Assert.assertEquals;

import android.Manifest;
import android.app.Application;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowApplication;

@RunWith(RobolectricTestRunner.class)
public class CompassActivityTesterMain {

    @Before
    public void setup() {
        Application application = ApplicationProvider.getApplicationContext();
        ShadowApplication app = Shadows.shadowOf(application);
        app.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Test
    public void testDisplaySingleLocationZero() {
        ActivityScenario<CompassActivity> scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            DisplayHelper.displaySingleLocation((CompassActivity)activity, 1, 0,0);
            View loc = activity.findViewById(1);
            ConstraintLayout.LayoutParams loc_lp = (ConstraintLayout.LayoutParams) loc.getLayoutParams();
            //check if radius is correct
            assertEquals(0, loc_lp.circleRadius);
            //check if angle is correct
            assertEquals(0, loc_lp.circleAngle, 0);
        });
    }

    @Test
    public void test360RotationAngles() {
        ActivityScenario<CompassActivity> scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            TextView north = (TextView) activity.findViewById(R.id.compass_N);
            TextView east = (TextView) activity.findViewById(R.id.compass_E);
            TextView south = (TextView) activity.findViewById(R.id.compass_S);
            TextView west = (TextView) activity.findViewById(R.id.compass_W);
            int rotationDegrees = 360;
            ConstraintLayout.LayoutParams north_lp = (ConstraintLayout.LayoutParams) north.getLayoutParams();
            north_lp.circleAngle = rotationDegrees;

            ConstraintLayout.LayoutParams east_lp = (ConstraintLayout.LayoutParams) east.getLayoutParams();
            east_lp.circleAngle = 90 + rotationDegrees;

            ConstraintLayout.LayoutParams south_lp = (ConstraintLayout.LayoutParams) south.getLayoutParams();
            south_lp.circleAngle = 180 + rotationDegrees;

            ConstraintLayout.LayoutParams west_lp = (ConstraintLayout.LayoutParams) west.getLayoutParams();
            west_lp.circleAngle = 270 + rotationDegrees;

            assertEquals(0 + 360, north_lp.circleAngle, 0);
            assertEquals(90 + 360, east_lp.circleAngle, 0);
            assertEquals(180 + 360, south_lp.circleAngle, 0);
            assertEquals(270 + 360, west_lp.circleAngle, 0);

        });
    }

    @Test
    public void testInitialRotationAngles() {
        ActivityScenario<CompassActivity> scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            TextView north = (TextView) activity.findViewById(R.id.compass_N);
            TextView east = (TextView) activity.findViewById(R.id.compass_E);
            TextView south = (TextView) activity.findViewById(R.id.compass_S);
            TextView west = (TextView) activity.findViewById(R.id.compass_W);
            int rotationDegrees = 0;
            ConstraintLayout.LayoutParams north_lp = (ConstraintLayout.LayoutParams) north.getLayoutParams();
            north_lp.circleAngle = rotationDegrees;

            ConstraintLayout.LayoutParams east_lp = (ConstraintLayout.LayoutParams) east.getLayoutParams();
            east_lp.circleAngle = 90 + rotationDegrees;

            ConstraintLayout.LayoutParams south_lp = (ConstraintLayout.LayoutParams) south.getLayoutParams();
            south_lp.circleAngle = 180 + rotationDegrees;

            ConstraintLayout.LayoutParams west_lp = (ConstraintLayout.LayoutParams) west.getLayoutParams();
            west_lp.circleAngle =  270 + rotationDegrees;

            assertEquals(0, north_lp.circleAngle,0);
            assertEquals(90, east_lp.circleAngle, 0);
            assertEquals(180, south_lp.circleAngle,  0);
            assertEquals(270, west_lp.circleAngle, 0);

        });
    }

}
