package com.example.compassproject;

import static org.junit.Assert.assertEquals;

import android.Manifest;
import android.app.Application;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowApplication;

@RunWith(RobolectricTestRunner.class)
public class CompassActivityTesterMain {
    Application application;
    @Before
    public void setup() {
        application = ApplicationProvider.getApplicationContext();
        ShadowApplication app = Shadows.shadowOf(application);
        app.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
        FriendEntryDatabase.getSingleton(application);
    }

    @After
    public void cleanUp(){
        FriendEntryDatabase.getSingleton(application).close();
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

    @Test
    public void testTextViewIfClose() {
        ActivityScenario<CompassActivity> scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            View view = DisplayHelper.displaySingleLocation(activity, 1,400, 0,20, 40, "test" );

            assertEquals(true, view instanceof TextView);

        });
    }

    @Test
    public void testCorrectRadiusForCircle() {
        ActivityScenario<CompassActivity> scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {

            ImageView compass = (ImageView) activity.findViewById(R.id.compass_face);
            int radius =  compass.getHeight() / 2;

            View view = DisplayHelper.displaySingleLocation(activity, 1, radius, 0,100, 40, "test" );

            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) view.getLayoutParams();

            assertEquals(radius, params.circleRadius);

            DisplayHelper.updateLocation(activity, view, radius, 0, 2000, 40,"test");

            params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
            assertEquals(radius, params.circleRadius);

        });
    }

    @Test
    public void testToggleGPSStatus(){
        ActivityScenario<CompassActivity> scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            // Get views
            TextView gpsIndicator = activity.findViewById(R.id.statusIndicator);
            TextView timeIndicator = activity.findViewById(R.id.timeIndicator);

            // Test GPS Status set to true
            activity.setGPSStatusTrue();

            assertEquals(Color.GREEN, gpsIndicator.getCurrentTextColor());
            assertEquals("Live", timeIndicator.getText());

            // Test GPS Status toggle to false
            long testTime = 1000L;
            activity.setSetGPSStatusFalse(testTime);

            assertEquals(Color.RED, gpsIndicator.getCurrentTextColor());
            assertEquals(Utilities.formatElapsedTime(testTime), timeIndicator.getText());

            // Test GPS Status toggle back to true
            activity.setGPSStatusTrue();

            assertEquals(Color.GREEN, gpsIndicator.getCurrentTextColor());
            assertEquals("Live", timeIndicator.getText());
        });
    }
}