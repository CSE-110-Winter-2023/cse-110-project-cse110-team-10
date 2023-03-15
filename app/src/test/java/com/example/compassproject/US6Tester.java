package com.example.compassproject;
import static org.junit.Assert.assertEquals;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
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
public class US6Tester {

    ZoomService zoomService;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        SharedPreferences preferences = context.getSharedPreferences("ZoomData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("zoom_level", 2);
        editor.apply();
        zoomService = new ZoomService(context);
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

            // View 1 will be 0.5 miles to North
            var orientation1 = 0;
            var distance1 = 0.5;

            // View 2 will be 5 miles to East
            var orientation2 = 90;
            var distance2 = 5;

            // Set UI as originally fully zoomed in
            assertEquals(2, zoomService.getZoomLevel());
            ZoomService.zoomIn();
            assertEquals(1, zoomService.getZoomLevel());
            View view1 = DisplayHelper.displaySingleLocation(activity, 1, radius, orientation1, distance1, zoomService.getZoomLevel(), "test");
            View view2 = DisplayHelper.displaySingleLocation(activity, 1, radius, orientation2, distance2, zoomService.getZoomLevel(), "test");
            assertEquals(true, view1 instanceof TextView); // View 1 is in range
            assertEquals(true, view2 instanceof CircleView); // View 2 is out of range

            // Tests zoom out

            ZoomService.zoomOut();
            assertEquals(2, zoomService.getZoomLevel());
            view1 = DisplayHelper.updateLocation(activity, view1, radius, orientation1, distance1, zoomService.getZoomLevel(), "test");
            view2 = DisplayHelper.updateLocation(activity, view2, radius, orientation2, distance2, zoomService.getZoomLevel(), "test");
            assertEquals(true, view1 instanceof TextView); // View 1 is in range
            assertEquals(true, view2 instanceof TextView); // View 2 is in range
        });
    }

    @Test
    public void test500PlusDistance(){
        ActivityScenario<CompassActivity> scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            // Zoomed in all the way so only friends within 1 mile will show name
            var radius = 400;

            // View 1 will be 0.5 miles to North
            var orientation1 = 0;
            var distance1 = 550;
            // Set UI as originally fully zoomed in
            assertEquals(2, zoomService.getZoomLevel());
            ZoomService.zoomOut();
            assertEquals(3, zoomService.getZoomLevel());
            View view1 = DisplayHelper.displaySingleLocation(activity, 1, radius, orientation1, distance1, zoomService.getZoomLevel(), "test");
            assertEquals(true, view1 instanceof CircleView); // View 1 is not in range

            // Tests zoom out

            ZoomService.zoomOut();
            assertEquals(4, zoomService.getZoomLevel());
            view1 = DisplayHelper.updateLocation(activity, view1, radius, orientation1, distance1, zoomService.getZoomLevel(), "test");
            assertEquals(true, view1 instanceof TextView); // View 1 is in range
        });
    }
}
