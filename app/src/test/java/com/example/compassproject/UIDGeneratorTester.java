package com.example.compassproject;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.compassproject.model.Location;
import com.example.compassproject.model.LocationAPI;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UIDGeneratorTester
{
    private LocationAPI singleton;

    @Before
    public void setup()
    {
        singleton = LocationAPI.provide();
    }

    @Test
    public void accurateUIDGenerated()
    {
        String UID = GenerateUID.UIDGeneratorHelper("Tim");

        String firstFourCharacters = UID.substring(0,4);
        String intNumbers = UID.substring(4);

        assertEquals("Tim-", firstFourCharacters);

        for(int i = 0; i<intNumbers.length(); i++)
        {
            int val = Integer.parseInt(String.valueOf(intNumbers.charAt(i)));
            assertTrue(val < 10 && val >= 0);
        }

    }


    @Test
    public void existingUIDGenerated()
    {
        singleton = LocationAPI.provide();
        String UID = GenerateUID.UIDGeneratorHelper("Tim");

        Location loc1 = new Location(UID,
                "Team10TestInput6000", "6000", 32.12, 74.12,
                true, "2023-03-05T12:00:00Z", "2023-03-05T18:30:00Z");
        singleton.putLocation(loc1);

        assertFalse(GenerateUID.checkUniqueUID(UID));
    }





}
