package com.example.compassproject;

import android.content.SharedPreferences;

public class UserInfo
{
    SharedPreferences preferences;

    public UserInfo(SharedPreferences preferences)
    {
        this.preferences = preferences;
    }

    public void setName(String name)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Name", name);
        editor.apply();
    }

    //TODO implement in US2
    public void setUID(String UID)
    {

    }

    public boolean hasName()
    {
        return !preferences.getString("Name", "").equals("");
    }

    public String getName()
    {
        return preferences.getString("Name", "");
    }

    //TODO implement in US2
    public String getUID()
    {
        return "";
    }






}
