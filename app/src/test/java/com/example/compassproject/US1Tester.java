package com.example.compassproject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowDialog;
import org.robolectric.shadows.ShadowIntent;

@RunWith(RobolectricTestRunner.class)
public class US1Tester
{
    ActivityScenario scenario;
    ActivityScenario userNameInputScenario;

    @Before
    public void setUpActivity()
    {
        scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        userNameInputScenario = ActivityScenario.launch(SetNameActivity.class);
        userNameInputScenario.moveToState(Lifecycle.State.CREATED);
        userNameInputScenario.moveToState(Lifecycle.State.STARTED);


    }

    @Test
    public void noUserNameEntered()
    {
        scenario.onActivity(activity -> {

            //tests that view correctly goes to input name view when no name is entered
            ShadowActivity shadowActivity = shadowOf(activity);
            Intent startedIntent = shadowActivity.getNextStartedActivity();
            ShadowIntent shadowIntent = shadowOf(startedIntent);
            assertEquals(SetNameActivity.class.getSimpleName(), shadowIntent.getIntentClass().getSimpleName());

        });
    }

    @Test
    public void inValidNameEntered()
    {
        scenario.onActivity(activity -> {

            //tests that view correctly goes to input name view when no name is entered
            ShadowActivity shadowActivity = shadowOf(activity);
            Intent startedIntent = shadowActivity.getNextStartedActivity();
            ShadowIntent shadowIntent = shadowOf(startedIntent);
            assertEquals(SetNameActivity.class.getSimpleName(), shadowIntent.getIntentClass().getSimpleName());

        });

        userNameInputScenario.onActivity(activity -> {
            TextView nameInputView = activity.findViewById(R.id.name_input);
            nameInputView.setText("");
            Button submitBtn = activity.findViewById(R.id.name_submit_btn);
            submitBtn.performClick();

            Dialog dialog = ShadowDialog.getLatestDialog();
            assertTrue(dialog.isShowing());


        });
    }

    @Test
    public void validNameEntered()
    {
        scenario.onActivity(activity -> {

            //tests that view correctly goes to input name view when no name is entered
            ShadowActivity shadowActivity = shadowOf(activity);
            Intent startedIntent = shadowActivity.getNextStartedActivity();
            ShadowIntent shadowIntent = shadowOf(startedIntent);
            assertEquals(SetNameActivity.class.getSimpleName(), shadowIntent.getIntentClass().getSimpleName());

        });

        userNameInputScenario.onActivity(activity -> {
            TextView nameInputView = activity.findViewById(R.id.name_input);
            nameInputView.setText("Julia");
            Button submitBtn = activity.findViewById(R.id.name_submit_btn);
            submitBtn.performClick();


            SharedPreferences preferences = activity.getSharedPreferences("UserData", Context.MODE_PRIVATE);
            UserInfo u1 = new UserInfo(preferences);

            assertTrue(u1.hasName());
            assertEquals("Julia",u1.getName());

        });
    }

    @Test
    public void nameEnteredBefore()
    {
        userNameInputScenario.onActivity(activity -> {
            TextView nameInputView = activity.findViewById(R.id.name_input);
            nameInputView.setText("Julia");
            Button submitBtn = activity.findViewById(R.id.name_submit_btn);
            submitBtn.performClick();


            SharedPreferences preferences = activity.getSharedPreferences("UserData", Context.MODE_PRIVATE);
            UserInfo u1 = new UserInfo(preferences);

            assertTrue(u1.hasName());
            assertEquals("Julia",u1.getName());

        });

        scenario.onActivity(activity -> {

            //tests that view correctly goes to input name view when no name is entered
            ShadowActivity shadowActivity = shadowOf(activity);
            Intent startedIntent = shadowActivity.getNextStartedActivity();
            ShadowIntent shadowIntent = shadowOf(startedIntent);
            assertEquals(CompassActivity.class.getSimpleName(), shadowIntent.getIntentClass().getSimpleName());

        });
    }



}
