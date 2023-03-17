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

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@RunWith(AndroidJUnit4.class)
public class FriendInputHandlingTester
{

    private MockWebServer server;
    @Before
    public void setUp(){
        server = new MockWebServer();

    }

    //invalid UID
    @Test
    public void testInvalidUID()
    {
        HttpUrl baseUrl = server.url("/v1/chat/");
        LocationAPI.changeEndpoint(baseUrl.toString());
        FriendEntry f1 = new FriendEntry("RandomNameThatWillNeverEverEverBeOnTheServer4000");
        assertFalse(CheckValidFriendUID.checkValidFriendUID(f1));

    }

    //valid UID
    @Test
    public void testValidUID()
    {
        try {
            server.start();
        } catch (Exception e){

        }

        HttpUrl baseUrl = server.url("/v1/chat/");
        LocationAPI.changeEndpoint(baseUrl.toString());

        server.enqueue(new MockResponse().setBody("{\"public_code\": \"tim\",\"private_code\": \"123-456-7890\",\"label\": \"Point Nemo\",\"latitude\": -48.876667,\"longitude\": -123.393333,\"is_listed_publicly\": false,\"created_at\": \"2023-03-17T05:17:25Z\",\"updated_at\": \"2023-03-17T05:17:25Z\"}" ));
        FriendEntry f1 = new FriendEntry("tim");
        assertTrue(CheckValidFriendUID.checkValidFriendUID(f1));

        try {
            server.shutdown();
        } catch (Exception e){

        }


    }


}
