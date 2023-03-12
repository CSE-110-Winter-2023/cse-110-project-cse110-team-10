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

        if(singleton.getLocationAsync(friend_uid) == null){
            return false;
        }
        else {
            return true;
        }
    }
}
