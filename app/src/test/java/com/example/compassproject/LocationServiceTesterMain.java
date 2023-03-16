package com.example.compassproject;

import static org.junit.Assert.assertEquals;

import android.Manifest;
import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.core.util.Pair;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowApplication;

@RunWith(RobolectricTestRunner.class)
public class LocationServiceTesterMain {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

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
    public void test_location_service() {
        double testLat = 32.0F;
        double testLong = 117.0F;

        var scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            var locationService = LocationService.singleton(activity);

            var mockLocation= new MutableLiveData<Pair<Double, Double>>();
            locationService.setMockLocationSource(mockLocation);
            activity.reobserveLocation();

            mockLocation.setValue(new Pair<>(testLat, testLong));
            assertEquals(testLat, activity.getLatitude(), 0);
            assertEquals(testLong, activity.getLongitude(), 0.1);

        });
    }

    @Test
    public void test_GPS_fix() {
        boolean testFixStatus = true;

        var scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            var locationService = LocationService.singleton(activity);

            var mockGPSFix= new MutableLiveData<Boolean>();
            locationService.setMockGPSFixSource(mockGPSFix);
            activity.reobserveLocation();

            mockGPSFix.setValue(testFixStatus);
            assertEquals(testFixStatus, mockGPSFix.getValue());

        });
    }
}