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
public class SavedUserOrientationTesterMain
{
    @Before
    public void setup() {
        Application application = ApplicationProvider.getApplicationContext();
        ShadowApplication app = Shadows.shadowOf(application);
        app.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Test
    public void testSavedUserOrientationCorrectly()
    {
        ActivityScenario<NewLocationActivity> scenario = ActivityScenario.launch(NewLocationActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            SharedPreferences preferences = activity.getSharedPreferences("Location", Context.MODE_PRIVATE);
            OrientationService os = OrientationService.singleton(activity);
            MutableLiveData<Float> angle = new MutableLiveData<>();
            angle.setValue((float) Math.PI);
            os.setMockOrientationSource(angle);

            SavedUserOrientation.saveUserOrientation(activity, os, preferences);

            assertEquals(180, SavedUserOrientation.getUserOrientation(preferences), 0.001);
        });
    }

    @Test
    public void testUpdatedUserOrientationCorrectly()
    {
        ActivityScenario<NewLocationActivity> scenario = ActivityScenario.launch(NewLocationActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            SharedPreferences preferences = activity.getSharedPreferences("Location", Context.MODE_PRIVATE);
            OrientationService os = OrientationService.singleton(activity);
            MutableLiveData<Float> angle = new MutableLiveData<>();
            angle.setValue((float) Math.PI);
            os.setMockOrientationSource(angle);

            SavedUserOrientation.saveUserOrientation(activity, os, preferences);
            assertEquals(180, SavedUserOrientation.getUserOrientation(preferences), 0.001);

            angle.setValue((float) (Math.PI * 1.5));
            os.setMockOrientationSource(angle);

            SavedUserOrientation.saveUserOrientation(activity, os, preferences);

            assertEquals(270, SavedUserOrientation.getUserOrientation(preferences), 0.001);
        });
    }

}


