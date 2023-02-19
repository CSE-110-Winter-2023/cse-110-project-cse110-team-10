package com.example.compassproject;
import static org.junit.Assert.assertEquals;
import static org.robolectric.Shadows.shadowOf;

import android.Manifest;
import android.app.Application;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
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

    @Test
    public void testRotate180(){
        float degreeN = DegreeCalculator.degreeBetweenCoordinates(0.0, 0.0, 10.0, 0.0);
        float degreeE = DegreeCalculator.degreeBetweenCoordinates(0.0, 0.0, 0, 10.0);
        ActivityScenario<CompassActivity> scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            OrientationService orientationService = OrientationService.singleton(activity);
            MutableLiveData<Float> mockOrientation = new MutableLiveData<Float>();
            orientationService.setMockOrientationSource(mockOrientation);
            activity.reobserveOrientation();
            View locN = DisplayHelper.displaySingleLocation((CompassActivity)activity, 1, 1, degreeN);
            View locE = DisplayHelper.displaySingleLocation((CompassActivity)activity, 1, 1, degreeE);
            //change phone's orientation
            mockOrientation.setValue((float) Math.PI);
            ConstraintLayout.LayoutParams loc_lpN = (ConstraintLayout.LayoutParams) locN.getLayoutParams();
            ConstraintLayout.LayoutParams loc_lpE = (ConstraintLayout.LayoutParams) locE.getLayoutParams();
            assertEquals(1, loc_lpN.circleRadius);
            assertEquals(degreeN, loc_lpN.circleAngle, 0);
            assertEquals(1, loc_lpE.circleRadius);
            assertEquals(degreeE, loc_lpE.circleAngle, 0);
        });
    }

    @Test
    public void testRotate90(){
        float degreeN = DegreeCalculator.degreeBetweenCoordinates(0.0, 0.0, 10.0, 0.0);
        float degreeE = DegreeCalculator.degreeBetweenCoordinates(0.0, 0.0, 0, 10.0);
        float degreeS = DegreeCalculator.degreeBetweenCoordinates(0.0, 0.0, -10.0, 0);

        ActivityScenario<CompassActivity> scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            OrientationService orientationService = OrientationService.singleton(activity);
            MutableLiveData<Float> mockOrientation = new MutableLiveData<Float>();
            orientationService.setMockOrientationSource(mockOrientation);
            activity.reobserveOrientation();
            View locN = DisplayHelper.displaySingleLocation((CompassActivity)activity, 1, 1, degreeN);
            View locE = DisplayHelper.displaySingleLocation((CompassActivity)activity, 1, 1, degreeE);
            View locS = DisplayHelper.displaySingleLocation((CompassActivity)activity, 1, 1, degreeS);
            //change phone's orientation
            mockOrientation.setValue((float) Math.PI/2);
            ConstraintLayout.LayoutParams loc_lpN = (ConstraintLayout.LayoutParams) locN.getLayoutParams();
            ConstraintLayout.LayoutParams loc_lpE = (ConstraintLayout.LayoutParams) locE.getLayoutParams();
            ConstraintLayout.LayoutParams loc_lpS = (ConstraintLayout.LayoutParams) locS.getLayoutParams();
            assertEquals(1, loc_lpN.circleRadius);
            assertEquals(degreeN, loc_lpN.circleAngle, 0);
            assertEquals(1, loc_lpE.circleRadius);
            assertEquals(degreeE, loc_lpE.circleAngle, 0);
            assertEquals(1, loc_lpS.circleRadius);
            assertEquals(degreeS, loc_lpS.circleAngle, 0);
        });
    }

}
