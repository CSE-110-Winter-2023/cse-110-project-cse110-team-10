package com.example.compassproject;

import static org.junit.Assert.assertEquals;

import android.Manifest;
import android.app.Application;
import android.view.View;

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
            DisplayHelper.displaySingleLocation((CompassActivity)activity, 1, 0,0, 5.0, 10.0, "Julia");
            View loc = activity.findViewById(1);
            ConstraintLayout.LayoutParams loc_lp = (ConstraintLayout.LayoutParams) loc.getLayoutParams();
            //check if radius is correct
            assertEquals(80, loc_lp.circleRadius);
            //check if angle is correct
            assertEquals(225, loc_lp.circleAngle, 0);
        });
    }


}
