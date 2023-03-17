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

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.internal.duplex.DuplexResponseBody;

@RunWith(AndroidJUnit4.class)
public class UIDGeneratorTester
{
    private MockWebServer server;
    @Before
    public void setup()
    {

        server = new MockWebServer();
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
        //singleton = LocationAPI.provide();
        String UID = GenerateUID.UIDGeneratorHelper("Tim");

        server.enqueue(new MockResponse().setBody("{\"public_code\": \"tim\",\"private_code\": \"123-456-7890\",\"label\": \"Point Nemo\",\"latitude\": -48.876667,\"longitude\": -123.393333,\"is_listed_publicly\": false,\"created_at\": \"2023-03-17T05:17:25Z\",\"updated_at\": \"2023-03-17T05:17:25Z\"}" ));

        try {
            server.start();
        } catch (Exception e){

        }

        HttpUrl baseUrl = server.url("/v1/chat/");
        LocationAPI.changeEndpoint(baseUrl.toString());

        assertFalse(GenerateUID.checkUniqueUID(UID));
        LocationAPI.changeEndpoint("https://socialcompass.goto.ucsd.edu/location");
        try {
            server.shutdown();
        } catch (Exception e){

        }
    }





}
