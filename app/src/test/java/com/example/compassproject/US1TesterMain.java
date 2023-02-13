package com.example.compassproject;

import static org.junit.Assert.assertEquals;
import static org.robolectric.Shadows.shadowOf;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowDialog;
import org.robolectric.shadows.ShadowIntent;


@RunWith(RobolectricTestRunner.class)
public class US1TesterMain {

    @Test
    public void testingPuttingValidLocationInNewLocation(){
        ActivityScenario<NewLocationActivity> scenario = ActivityScenario.launch(NewLocationActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            TextView coordsBtn = activity.findViewById(R.id.coordinateInput);
            coordsBtn.setText("33.0, 117.0");
            TextView labelBtn = activity.findViewById(R.id.labelInput);
            labelBtn.setText("Julia's House");
            Button submitBtn = activity.findViewById(R.id.submit_location_btn);
            submitBtn.performClick();

            SharedPreferences preferences = activity.getSharedPreferences("LocationData", Context.MODE_PRIVATE);
            SavedLocations sl = new SavedLocations(preferences);
            assertEquals(1, sl.getNumLocations());
            assertEquals(33.0, sl.getLatitude(0), 0.1);
            assertEquals(117.0, sl.getLongitude(0), 0.1);

            ShadowActivity shadowActivity = shadowOf(activity);
            Intent startedIntent = shadowActivity.getNextStartedActivity();
            ShadowIntent shadowIntent = shadowOf(startedIntent);
            assertEquals(MainActivity.class.getSimpleName(), shadowIntent.getIntentClass().getSimpleName());
        });
    }

    @Test
    public void testingPuttingInValidLocationInNewLocation(){
        ActivityScenario<NewLocationActivity> scenario = ActivityScenario.launch(NewLocationActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            TextView coordsBtn = activity.findViewById(R.id.coordinateInput);
            coordsBtn.setText("");
            TextView labelBtn = activity.findViewById(R.id.labelInput);
            labelBtn.setText("Julia's House");
            Button submitBtn = activity.findViewById(R.id.submit_location_btn);
            submitBtn.performClick();

            // Check that the dialog is shown and has valid content.
            Dialog dialog = ShadowDialog.getLatestDialog();
            assertEquals(true, dialog.isShowing());
        });
    }


}
