package com.example.compassproject;

import static org.junit.Assert.assertEquals;

import android.Manifest;
import android.app.Application;
import android.graphics.Color;
import android.widget.TextView;

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
public class US5TesterMain {
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
    public void testGPSStatusLostFor30s(){
        ActivityScenario<CompassActivity> scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            // Get views
            TextView gpsIndicator = activity.findViewById(R.id.statusIndicator);
            TextView timeIndicator = activity.findViewById(R.id.timeIndicator);

            // We have connection
            activity.setGPSStatusTrue();

            // We lost connection for 30s
            LocationService ls = LocationService.singleton(activity);
            ls.setMockElapsedTime(30000);
            ls.satelliteStatusChanged();

            activity.reobserveGPSFix();

            assertEquals(Color.GREEN, gpsIndicator.getCurrentTextColor());
            assertEquals("Live", timeIndicator.getText());
        });
        scenario.close();
    }

    @Test
    public void testGPSStatusLostFor2m(){
        ActivityScenario<CompassActivity> scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            // Get views
            TextView gpsIndicator = activity.findViewById(R.id.statusIndicator);
            TextView timeIndicator = activity.findViewById(R.id.timeIndicator);

            // WE have connection
            activity.setGPSStatusTrue();

            // We lost connection for 30s
            LocationService ls = LocationService.singleton(activity);
            ls.setMockElapsedTime(120000);

            ls.satelliteStatusChanged();
            activity.reobserveGPSFix();

            assertEquals(Color.RED, gpsIndicator.getCurrentTextColor());
            assertEquals("2m", timeIndicator.getText());
        });
        scenario.close();
    }

}
