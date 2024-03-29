package com.example.compassproject;

import android.content.SharedPreferences;

public class UserInfo
{
    SharedPreferences preferences;

    public UserInfo(SharedPreferences preferences)
    {
        this.preferences = preferences;
    }

    //stores user name in shared preferences
    public void setName(String name)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Name", name);
        editor.apply();
    }

    //stores user uid in shared preferences
    //TODO implement in US2
    public void setUID(String UID)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("UID", UID);
        editor.apply();
    }

    //stores user's private code in shared preference
    public void setPrivateCode(String privateCode)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("PrivateCode", privateCode);
        editor.apply();
    }


    //checks if user has inputted a name
    public boolean hasName()
    {
        return !preferences.getString("Name", "").equals("");
    }

    //returns user name
    public String getName()
    {
        return preferences.getString("Name", "");
    }

    //returns user UID
    //TODO implement in US2
    public String getUID()
    {
        return preferences.getString("UID", "");
    }

    // returns getPrivateCode
    public String getPrivateCode()
    {
        return preferences.getString("PrivateCode", "");
    }






}
