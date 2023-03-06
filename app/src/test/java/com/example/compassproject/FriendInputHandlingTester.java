package com.example.compassproject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.compassproject.model.Location;

import androidx.test.ext.junit.runners.AndroidJUnit4;


import com.example.compassproject.model.LocationAPI;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FriendInputHandlingTester
{
    private LocationAPI singleton;

    @Before
    public void setUp(){
        singleton = LocationAPI.provide();

    }

    //invalid UID
    @Test
    public void testInvalidUID()
    {
        FriendEntry f1 = new FriendEntry("RandomNameThatWillNeverEverEverBeOnTheServer4000");
        assertFalse(CheckValidFriendUID.checkValidFriendUID(f1));

    }

    //valid UID
    @Test
    public void testValidUID()
    {
        FriendEntry f1 = new FriendEntry("RandomNameThatWillAlwaysAlwaysBeOnTheServer4000");

        Location loc1 = new Location("RandomNameThatWillAlwaysAlwaysBeOnTheServer4000",
                "Team10TestInput4000", "4000", 32.12, 74.12,
                true, "2023-03-05T12:00:00Z", "2023-03-05T18:30:00Z");
        singleton.putLocation(loc1);

        assertTrue(CheckValidFriendUID.checkValidFriendUID(f1));

    }


}
