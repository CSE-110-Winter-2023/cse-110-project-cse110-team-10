package com.example.compassproject;

import static org.junit.Assert.assertEquals;

import android.view.View;
import android.widget.TextView;
//import androidx.test.ext.junit.rules.ActivityScenarioRule;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class NewLocationActivityTesterMain {
    /*
      1) Coordinate empty, label empty
      2) Coordinate incorrect, label empty
      3) Coordinate correct, label empty
      4) Coordinate empty, label filled
      5) Coordinate incorrect, label filled
      6) Coordinate correct, label filled
     */
    //@Rule
    //public ActivityScenarioRule rule = new ActivityScenarioRule<>(NewLocationActivity.class);
/*
    @Test
    public void testEmptyCoordinateAndEmptyLabel() {

        //rule.getScenario().onActivity(activity -> {
        //assertEquals(false, activity.isInputValid("",""));
        try (ActivityScenario<NewLocationActivity> scenario = ActivityScenario.launch(NewLocationActivity.class)) {
            scenario.moveToState(Lifecycle.State.CREATED);
            scenario.onActivity(activity -> {
                TextView coordinateView = activity.findViewById(R.id.coordinateInput);
                coordinateView.setText("");
                assertEquals("", coordinateView.getText());
            });
        }
    }

 */
}
