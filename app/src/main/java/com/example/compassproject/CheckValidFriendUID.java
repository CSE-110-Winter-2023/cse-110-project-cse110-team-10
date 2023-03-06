package com.example.compassproject;

import android.app.Activity;

import com.example.compassproject.model.LocationAPI;

public class CheckValidFriendUID
{
    public static boolean checkValidFriendUID(FriendEntry friend)
    {
        String friend_uid = friend.uid;

        //define location API singleton
        LocationAPI singleton = LocationAPI.provide();

        //check if valid UID
        if(singleton.getLocationAsync(friend_uid) == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}
