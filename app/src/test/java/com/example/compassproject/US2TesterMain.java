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
public class US2TesterMain
{
    @Before
    public void setup() {
        Application application = ApplicationProvider.getApplicationContext();
        ShadowApplication app = Shadows.shadowOf(application);
        app.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Test
    public void testVaryingLocations()
    {
        ActivityScenario<CompassActivity> scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {


            int radius = 1;
            //calculate north degree
            float degreeN = DegreeCalculator.degreeBetweenCoordinates(0.0, 0.0, 10.0, 0.0);
            //calculate south degree
            float degreeS = DegreeCalculator.degreeBetweenCoordinates(0.0, 0.0, -10.0, 0.0);
            //calculate east degree
            float degreeE = DegreeCalculator.degreeBetweenCoordinates(0.0, 0.0, 0, 10.0);
            //calculate west degree
            float degreeW = DegreeCalculator.degreeBetweenCoordinates(0.0, 0.0, 0, -10.0);


            View locN = DisplayHelper.displaySingleLocation((CompassActivity)activity, 1, radius, degreeN);
            ConstraintLayout.LayoutParams loc_lpN = (ConstraintLayout.LayoutParams) locN.getLayoutParams();
            //check if radius is correct
            assertEquals(1, loc_lpN.circleRadius);
            //check if angle is correct
            assertEquals(degreeN, loc_lpN.circleAngle, 0);

            View locE = DisplayHelper.displaySingleLocation((CompassActivity)activity, 1, radius, degreeE);
            ConstraintLayout.LayoutParams loc_lpE = (ConstraintLayout.LayoutParams) locE.getLayoutParams();
            //check if radius is correct
            assertEquals(1, loc_lpE.circleRadius);
            //check if angle is correct
            assertEquals(degreeE, loc_lpE.circleAngle, 0);

            View locW = DisplayHelper.displaySingleLocation((CompassActivity)activity, 1, radius, degreeW);
            ConstraintLayout.LayoutParams loc_lpW = (ConstraintLayout.LayoutParams) locW.getLayoutParams();
            //check if radius is correct
            assertEquals(1, loc_lpW.circleRadius);
            //check if angle is correct
            assertEquals(degreeW, loc_lpW.circleAngle, 0);

            View locS = DisplayHelper.displaySingleLocation((CompassActivity)activity, 1, radius, degreeS);
            ConstraintLayout.LayoutParams loc_lpS = (ConstraintLayout.LayoutParams) locS.getLayoutParams();
            //check if radius is correct
            assertEquals(1, loc_lpS.circleRadius);
            //check if angle is correct
            assertEquals(degreeS, loc_lpS.circleAngle, 0);
        });
    }
}
