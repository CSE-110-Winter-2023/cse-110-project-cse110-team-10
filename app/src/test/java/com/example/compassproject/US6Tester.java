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

    @Test
    public void testOneMile(){
        ActivityScenario<CompassActivity> scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            var radius = 400;
            var orientation1 = 0;
            var distance1 = 1;
            assertEquals(2, zoomService.getZoomLevel());
            ZoomService.zoomIn();
            assertEquals(1, zoomService.getZoomLevel());
            View view1 = DisplayHelper.displaySingleLocation(activity, 1, radius, orientation1, distance1, zoomService.getZoomLevel(), "test");
            assertEquals(true, view1 instanceof CircleView);
        });
    }
    @Test
    public void test500PlusDistance(){
        ActivityScenario<CompassActivity> scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            var radius = 400;
            var orientation1 = 0;
            var distance1 = 550;
            assertEquals(2, zoomService.getZoomLevel());
            ZoomService.zoomOut();
            assertEquals(3, zoomService.getZoomLevel());
            View view1 = DisplayHelper.displaySingleLocation(activity, 1, radius, orientation1, distance1, zoomService.getZoomLevel(), "test");
            assertEquals(true, view1 instanceof CircleView); // View 1 is not in range
            ZoomService.zoomOut();
            assertEquals(4, zoomService.getZoomLevel());
            view1 = DisplayHelper.updateLocation(activity, view1, radius, orientation1, distance1, zoomService.getZoomLevel(), "test");
            assertEquals(true, view1 instanceof TextView); // View 1 is in range
        });
    }
}
