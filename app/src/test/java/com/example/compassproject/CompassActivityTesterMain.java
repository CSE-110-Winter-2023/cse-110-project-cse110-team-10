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
/*
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

 */

    @Before
    public void setup() {
        Application application = ApplicationProvider.getApplicationContext();
        ShadowApplication app = Shadows.shadowOf(application);
        app.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Test
    public void testCircleViewIfFar() {
        ActivityScenario<CompassActivity> scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            View view = DisplayHelper.displaySingleLocation(activity, 1,400, 0,100, 40, "test" );

            assertEquals(true, view instanceof CircleView);

        });
    }

    public void testTextViewIfClose() {
        ActivityScenario<CompassActivity> scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            View view = DisplayHelper.displaySingleLocation(activity, 1,400, 0,20, 40, "test" );

            assertEquals(true, view instanceof TextView);

        });
    }
}
