package com.example.compassproject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.Manifest;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.core.util.Pair;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import com.example.compassproject.model.Location;
import com.example.compassproject.model.LocationAPI;
import com.example.compassproject.model.LocationDao;
import com.example.compassproject.model.LocationDatabase;
import com.example.compassproject.model.LocationRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowDialog;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

@RunWith(RobolectricTestRunner.class)
public class US2Tester {


    private MockWebServer server;
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUpActivity()
    {
        Application application = ApplicationProvider.getApplicationContext();
        ShadowApplication app = Shadows.shadowOf(application);
        app.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);

        server = new MockWebServer();
    }

    @Test
    public void testUIDProperlyDisplayedAndStored()
    {

        ActivityScenario userNameInputScenario = ActivityScenario.launch(SetNameActivity.class);
        userNameInputScenario.moveToState(Lifecycle.State.CREATED);
        userNameInputScenario.moveToState(Lifecycle.State.STARTED);
        //mocks inputting name and generating UID and private code
        userNameInputScenario.onActivity(activity -> {
            TextView nameInputView = activity.findViewById(R.id.name_input);
            nameInputView.setText("Julia");
            Button submitBtn = activity.findViewById(R.id.name_submit_btn);
            submitBtn.performClick();
        });

        double testLat = 32.0F;
        double testLong = 117.0F;

        var compassActivityScenario = ActivityScenario.launch(CompassActivity.class);
        compassActivityScenario.moveToState(Lifecycle.State.CREATED);
        compassActivityScenario.moveToState(Lifecycle.State.STARTED);

        //checks if initially stored on compass correctly
        compassActivityScenario.onActivity(activity -> {
            SharedPreferences preferences = activity.getSharedPreferences("UserData", Context.MODE_PRIVATE);
            UserInfo u1 = new UserInfo(preferences);

            try {
                server.start();
            } catch (Exception e){

            }

            HttpUrl baseUrl = server.url("/v1/chat/");
            LocationAPI.changeEndpoint(baseUrl.toString());

            String userUID = u1.getUID();

            String firstSixCharacters = userUID.substring(0,6);
            String userName = userUID.substring(0,5);

            assertEquals("Julia-", firstSixCharacters);
            assertEquals(11, userUID.length());

            var locationService = LocationService.singleton(activity);
            var mockLocation= new MutableLiveData<Pair<Double, Double>>();
            locationService.setMockLocationSource(mockLocation);

            activity.reobserveLocation();

            mockLocation.setValue(new Pair<>(testLat, testLong));

            activity.updateCoordinates(mockLocation.getValue());
            try {
                 RecordedRequest request1 = server.takeRequest();
                 assertEquals("PUT /v1/chat//" + userUID + " HTTP/1.1", request1.toString());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            LocationAPI.changeEndpoint("https://socialcompass.goto.ucsd.edu/location");
            try {
                server.shutdown();
            } catch (Exception e){

            }
        });

        //test if UID is displayed correctly
        ActivityScenario addFriendsScenario = ActivityScenario.launch(AddFriendsActivity.class);
        addFriendsScenario.moveToState(Lifecycle.State.CREATED);
        addFriendsScenario.moveToState(Lifecycle.State.STARTED);

        addFriendsScenario.onActivity(activity -> {
            SharedPreferences preferences = activity.getSharedPreferences("UserData", Context.MODE_PRIVATE);
            UserInfo u1 = new UserInfo(preferences);
            TextView textView = activity.findViewById(R.id.your_uid);
            assertEquals(u1.getUID(), textView.getText().toString());
        });
    }


}
