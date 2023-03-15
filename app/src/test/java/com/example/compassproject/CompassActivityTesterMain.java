package com.example.compassproject;

import static org.junit.Assert.assertEquals;

import android.Manifest;
import android.app.Application;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
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
    public void testCircleViewIfFar() {
        ActivityScenario<CompassActivity> scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            View view = DisplayHelper.displaySingleLocation(activity, 1,400, 0,100, 2, "test" );
            assertEquals(true, view instanceof CircleView);

        });
    }

    @Test
    public void testTextViewIfClose() {
        ActivityScenario<CompassActivity> scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            View view = DisplayHelper.displaySingleLocation(activity, 1,400, 0,20, 3, "test" );
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
            View view = DisplayHelper.displaySingleLocation(activity, 1, radius, 0,100, 2, "test" );
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
            assertEquals(radius, params.circleRadius);
            DisplayHelper.updateLocation(activity, view, radius, 0, 2000, 2,"test");
            params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
            assertEquals(radius, params.circleRadius);

        });
    }
}