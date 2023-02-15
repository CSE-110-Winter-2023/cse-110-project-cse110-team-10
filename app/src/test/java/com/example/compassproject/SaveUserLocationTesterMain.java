package com.example.compassproject;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.assertEquals;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.renderscript.ScriptGroup;
import android.util.Pair;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import android.util.Pair;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowApplication;


@RunWith(RobolectricTestRunner.class)
    public class SaveUserLocationTesterMain
    {
        @Before
        public void setup() {
            Application application = ApplicationProvider.getApplicationContext();
            ShadowApplication app = Shadows.shadowOf(application);
            app.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        @Test
        public void testSavedUserLocationCorrectly()
        {
            ActivityScenario<NewLocationActivity> scenario = ActivityScenario.launch(NewLocationActivity.class);
            scenario.moveToState(Lifecycle.State.CREATED);
            scenario.moveToState(Lifecycle.State.STARTED);
            scenario.onActivity(activity -> {

                MockLocationService m1 = new MockLocationService(activity);
                MutableLiveData<Pair<Double,Double>> coord = new MutableLiveData<>();
                coord.setValue(new Pair(-12.924239968315463, -18.354989375000002));

                m1.setLocation(coord);

                SavedUserLocation.saveUserLoc(activity, m1);

                assertEquals(-12.924239968315463, SavedUserLocation.getUserLatitude(), 0.001);
                assertEquals(-18.354989375000002, SavedUserLocation.getUserLongitude(), 0.001);
            });
        }

        @Test
        public void testUpdatedUserLocationCorrectly()
        {
            ActivityScenario<NewLocationActivity> scenario = ActivityScenario.launch(NewLocationActivity.class);
            scenario.moveToState(Lifecycle.State.CREATED);
            scenario.moveToState(Lifecycle.State.STARTED);
            scenario.onActivity(activity -> {
                MockLocationService m1 = new MockLocationService(activity);
                MutableLiveData<Pair<Double,Double>> coord = new MutableLiveData<>();
                coord.setValue(new Pair(33.344281538848165, -97.58838781250005));

                m1.setLocation(coord);

                SavedUserLocation.saveUserLoc(activity, m1);

                assertEquals(33.344281538848165, SavedUserLocation.getUserLatitude(), 0.001);
                assertEquals(-97.58838781250005, SavedUserLocation.getUserLongitude(), 0.001);

                MutableLiveData<Pair<Double,Double>> coord2 = new MutableLiveData<>();
                coord2.setValue(new Pair(53.340702936615486, -87.06348546874996));

                m1.setLocation(coord2);

                SavedUserLocation.saveUserLoc(activity, m1);

                assertEquals(53.340702936615486, SavedUserLocation.getUserLatitude(), 0.001);
                assertEquals(-87.06348546874996, SavedUserLocation.getUserLongitude(), 0.001);

            });
        }

    }


