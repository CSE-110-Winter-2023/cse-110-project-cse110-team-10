package com.example.compassproject;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

/*
Since the input has checked it has to be
valid to be able to process, so definitely the input
will contains latitude, longitude, and the label.
Our test will only test if we save data correctly.
 */
@RunWith(RobolectricTestRunner.class)
public class SaveLocationTesterMain {

    ActivityScenario scenario;
    @Before
    public void setUpActivity(){
        scenario = ActivityScenario.launch(NewLocationActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
    }
    @Test
    public void testSavedDataCorrectly(){
        scenario.onActivity(activity -> {
            SharedPreferences preferences = activity.getSharedPreferences("Location", Context.MODE_PRIVATE);
            SavedLocations sl = new SavedLocations(preferences);
            sl.saveLocation("33.0, 117.0", "Julia's House");
            assertEquals(1, preferences.getInt("numLocs", -1));
            assertEquals(33.0, preferences.getFloat("latitude0", -7), 0);
            assertEquals(117.0, preferences.getFloat("longitude0", -10), 0);
            assertEquals("Julia's House", preferences.getString("label0", ""));

        });
    }

    /*
            Test numLocs if multiple locations
            Test numLocs if no locations
            Test if latitude, and longitude are saved correctly
            Test if label is saved correctly
    */

    @Test
    public void testNumLocsWhenMultipleLocation(){
        scenario.onActivity(activity -> {
            SharedPreferences preferences = activity.getSharedPreferences("Location", Context.MODE_PRIVATE);
            SavedLocations sl = new SavedLocations(preferences);
            sl.saveLocation("33.0, 117.0", "Julia's House");
            sl.saveLocation("34.0, 118.0", "Parents House");
            sl.saveLocation("35.0, 119.0", "Friend's House");
            assertEquals(3,sl.getNumLocations());
        });

    }

    @Test
    public void testNumLocsWhenNoLocation() {
        scenario.onActivity(activity -> {
            SharedPreferences preferences = activity.getSharedPreferences("Location", Context.MODE_PRIVATE);
            SavedLocations sl = new SavedLocations(preferences);
            assertEquals(0, sl.getNumLocations());
        });
    }

    @Test
    public void testLatitudeAndLongitude() {
        scenario.onActivity(activity -> {
            SharedPreferences preferences = activity.getSharedPreferences("Location", Context.MODE_PRIVATE);
            SavedLocations sl = new SavedLocations(preferences);
            sl.saveLocation("33.0, 117.0", "Julia's House");
            sl.saveLocation("34.0, 118.0", "Parents House");
            sl.saveLocation("35.0, 119.0", "Friend's House");
            assertEquals(34.0, sl.getLatitude(1), 0);
            assertEquals(118.0, sl.getLongitude(1), 0);
        });
    }

    @Test
    public void testLabel(){
        scenario.onActivity(activity -> {
            SharedPreferences preferences = activity.getSharedPreferences("Location", Context.MODE_PRIVATE);
            SavedLocations sl = new SavedLocations(preferences);
            sl.saveLocation("33.0, 117.0", "Julia's House");
            sl.saveLocation("34.0, 118.0", "Parents House");
            sl.saveLocation("35.0, 119.0", "Friend's House");
            assertEquals("Friend's House", sl.getLabel(2));
            assertEquals("Julia's House", sl.getLabel(0));
        });
    }

}
