package com.example.compassproject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Lifecycle;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.bouncycastle.math.ec.rfc7748.X448;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowDialog;
import org.robolectric.shadows.ShadowIntent;

import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class US3Tester {
    private FriendEntryDao dao;
    private FriendEntryDatabase db;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, FriendEntryDatabase.class)
                .allowMainThreadQueries()
                .build();
        FriendEntryDatabase.injectTestDatabase(db);
        dao = db.friendEntryDao();
    }

    @Test
    public void testingInvalidUID() {
        ActivityScenario<AddFriendsActivity> scenario = ActivityScenario.launch(AddFriendsActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            TextView friendUIDView = activity.findViewById(R.id.friend_uid_input);
            friendUIDView.setText("RandomNameThatWillNeverEverEverBeOnTheServer4000");
            Button submitBtn = activity.findViewById(R.id.friend_submit_btn);
            submitBtn.performClick();

            //check if stored in UID database
            List<FriendEntry> entries = dao.getAll();
            assertEquals(0, entries.size());

            //check activity
            Dialog dialog = ShadowDialog.getLatestDialog();
            assertTrue(dialog.isShowing());

        });
    }

    @Test
    public void testingPuttingValidUID() {

        ActivityScenario<AddFriendsActivity> scenario = ActivityScenario.launch(AddFriendsActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            TextView friendUIDView = activity.findViewById(R.id.friend_uid_input);
            friendUIDView.setText("RandomNameThatWillAlwaysAlwaysBeOnTheServer4000");
            Button submitBtn = activity.findViewById(R.id.friend_submit_btn);
            submitBtn.performClick();


            //check if stored in UID database
            List<FriendEntry> entries = dao.getAll();
            assertEquals(1, entries.size());
            assertEquals("RandomNameThatWillAlwaysAlwaysBeOnTheServer4000", entries.get(0).uid);

            ShadowActivity shadowActivity = shadowOf(activity);
            Intent startedIntent = shadowActivity.getNextStartedActivity();
            ShadowIntent shadowIntent = shadowOf(startedIntent);
            assertEquals(CompassActivity.class.getSimpleName(), shadowIntent.getIntentClass().getSimpleName());
        });
    }




}
