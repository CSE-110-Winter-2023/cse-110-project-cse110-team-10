package com.example.compassproject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.compassproject.model.Location;
import com.example.compassproject.model.LocationAPI;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LocationAPITesterMain {

    LocationAPI singleTon;
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Before
    public void setUp(){
        singleTon = LocationAPI.provide();

    }

    @Test
    public void testGetAndPutFromServer(){
        singleTon = LocationAPI.provide();
        LocationAPI.changeEndpoint("https://socialcompass.goto.ucsd.edu/location");
        Location loc1 = new Location("kgupta", "Team10TestInput", "Kanishk", 32.12, 74.12, true, "2023-02-18T12:00:00Z", "2023-02-18T18:30:00Z");
        singleTon.putLocation(loc1);
        Location loc2 = singleTon.getLocation(loc1.public_code);
        assertEquals(loc1.label, loc2.label);
        assertEquals(loc1.latitude, loc2.latitude, 0.1);
        assertEquals(loc1.longitude, loc2.longitude, 0.2);
    }

    @Test
    public void testDelete(){
        singleTon = LocationAPI.provide();
        LocationAPI.changeEndpoint("https://socialcompass.goto.ucsd.edu/location");
        Location loc1 = new Location("kgupta", "Team10TestInput", "Kanishk", 32.12, 74.12, true, "2023-02-18T12:00:00Z", "2023-02-18T18:30:00Z");
        singleTon.putLocation(loc1);
        Location loc2 = singleTon.getLocation(loc1.public_code);
        assertEquals(loc1.label, loc2.label);
        assertEquals(loc1.latitude, loc2.latitude, 0.1);
        assertEquals(loc1.longitude, loc2.longitude, 0.2);
        singleTon.deleteLocation(loc1);
        assertNull(singleTon.getLocation(loc1.public_code));

    }
}
