package com.example.compassproject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class UserInfoTester {
    ActivityScenario scenario;

    @Before
    public void setUpActivity()
    {
        scenario = ActivityScenario.launch(SetNameActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
    }

    @Test
    public void testSaveNameCorrectly()
    {
        scenario.onActivity(activity -> {
            SharedPreferences preferences = activity.getSharedPreferences("Test", Context.MODE_PRIVATE);
            UserInfo u1 = new UserInfo(preferences);

            u1.setName("Julia");

            assertEquals("Julia", u1.getName());
            assertTrue(u1.hasName());
        });
    }


    //TODO IN US2
    @Test
    public void testSaveUIDCorrectly()
    {
        scenario.onActivity(activity -> {
            SharedPreferences preferences = activity.getSharedPreferences("Test", Context.MODE_PRIVATE);
            UserInfo u1 = new UserInfo(preferences);

            u1.setUID("JuliaUID");

            assertEquals("JuliaUID", u1.getUID());
        });
    }


}
