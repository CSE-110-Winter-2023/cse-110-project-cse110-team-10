package com.example.compassproject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.mockito.Mockito;
import android.os.StrictMode;

public class CompassActivityTesterMain {

    ActivityScenario scenario;
    @Before
    public void setUpCompassActivity() {
        StrictMode strictMode = Mockito.mock(StrictMode.class);
        Mockito.when(strictMode.allowThreadDiskReads()).thenReturn(null);
    }

    @Test
    public void testDisplaySingleLocation() {
        scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {

            DisplayHelper.displaySingleLocation((CompassActivity)activity, 1, 100,90);
            View loc = activity.findViewById(1);

            ConstraintLayout.LayoutParams loc_lp = (ConstraintLayout.LayoutParams) loc.getLayoutParams();
            //check if radius is correct
            assertEquals(100, loc_lp.circleRadius);

            //check if angle is correct
            assertEquals(90, loc_lp.circleAngle);

        });

    }
}
