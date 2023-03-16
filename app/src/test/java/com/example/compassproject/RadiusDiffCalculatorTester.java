package com.example.compassproject;


import static org.junit.Assert.assertEquals;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;

import com.example.compassproject.model.Location;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(RobolectricTestRunner.class)
public class RadiusDiffCalculatorTester
{
    @Test
    public void identifyStackedLocations()
    {
        ActivityScenario<CompassActivity> scenario = ActivityScenario.launch(CompassActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {

            MutableLiveData<Location> loc1 = new MutableLiveData<Location>
                    (new Location("kgupta", "Team10TestInput", "Kanishk",
                            11.0F, 11.0F, true, "2023-02-18T12:00:00Z", "2023-02-18T18:30:00Z"));
            MutableLiveData<Location> loc2 = new MutableLiveData<Location>
                    (new Location("wob", "Team10TestInput1000", "wob",
                            11.0F, 11.0F, true, "2023-02-18T12:00:00Z", "2023-02-18T18:30:00Z"));
            MutableLiveData<Location> loc3 = new MutableLiveData<Location>
                    (new Location("tim", "Team10TestInput2000", "tim",
                            -11.0F, -11.0F, true, "2023-02-18T12:00:00Z", "2023-02-18T18:30:00Z"));

            activity.locationArray.add(loc1);
            activity.locationArray.add(loc2);
            activity.locationArray.add(loc3);

            Map<String, Integer> radiusDiffList = activity.calculateRadiusDiff();

            assertEquals((long)-30, (long) radiusDiffList.get(loc1.getValue().public_code));
            assertEquals((long) 30, (long) radiusDiffList.get(loc2.getValue().public_code));
            assertEquals((long) 0, (long) radiusDiffList.get(loc3.getValue().public_code));



        });

    }
}
