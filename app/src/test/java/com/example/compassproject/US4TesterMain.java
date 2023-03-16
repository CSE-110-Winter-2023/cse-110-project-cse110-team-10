package com.example.compassproject;
import static org.junit.Assert.assertEquals;

import android.Manifest;
import android.app.Application;
import android.view.View;
import android.widget.TextView;

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
public class US4TesterMain {

    @Before
    public void setup() {
        Application application = ApplicationProvider.getApplicationContext();
        ShadowApplication app = Shadows.shadowOf(application);
        app.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    // Tests if locations change from Circle to Text when both zoom in and zoom out
    @Test
    public void testFriendsAreInDifferentLocationsAndDifferentZones() {
        ActivityScenario<CompassActivity> scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            // Zoomed in all the way so only friends within 1 mile will show name
            var radius = 400;
            var zoomLevel = 1;

            // View 1 will be 0.5 miles to North
            var orientation1 = 0;
            var distance1 = 0.5;

            // View 2 will be 5 miles to East
            var orientation2 = 90;
            var distance2 = 5;

            // Set UI as originally fully zoomed in

            View view1 = DisplayHelper.displaySingleLocation(activity, 1, radius, orientation1, distance1, 1, "test", 0);
            View view2 = DisplayHelper.displaySingleLocation(activity, 1, radius, orientation2, distance2, 1, "test", 0);
            assertEquals(true, view1 instanceof TextView); // View 1 is in range
            assertEquals(true, view2 instanceof CircleView); // View 2 is out of range

            // Tests zoom out

            zoomLevel = 2; // Zoom out to include all locations within 10 miles
            view1 = DisplayHelper.updateLocation(activity, view1, radius, orientation1, distance1, zoomLevel, "test", 0);
            view2 = DisplayHelper.updateLocation(activity, view2, radius, orientation2, distance2, zoomLevel, "test", 0);
            assertEquals(true, view1 instanceof TextView); // View 1 is in range
            assertEquals(true, view2 instanceof TextView); // View 2 is in range

            // Tests zoom in

            zoomLevel = 1; // Zoom out to include all locations within 1 mile
            view1 = DisplayHelper.updateLocation(activity, view1, radius, orientation1, distance1, zoomLevel, "test", 0);
            view2 = DisplayHelper.updateLocation(activity, view2, radius, orientation2, distance2, zoomLevel, "test", 0);
            assertEquals(true, view1 instanceof TextView); // View 1 is in range
            assertEquals(true, view2 instanceof CircleView); // View 2 is in range
        });
    }
}
