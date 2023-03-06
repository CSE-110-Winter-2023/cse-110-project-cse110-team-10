package com.example.compassproject;

import android.app.Activity;

import com.example.compassproject.model.Location;
import com.example.compassproject.model.LocationAPI;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CheckValidFriendUID
{
    public static boolean checkValidFriendUID(FriendEntry friend)
    {
        String friend_uid = friend.uid;
        LocationAPI singleton = LocationAPI.provide();

        Future<Location> gotLocation = singleton.getLocationAsync(friend_uid);
        while(!gotLocation.isDone()) {;}

        //check if valid UID
        try {
            if(singleton.getLocationAsync(friend_uid).get(1, TimeUnit.SECONDS) == null)
            {
                return false;
            }
            else
            {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
