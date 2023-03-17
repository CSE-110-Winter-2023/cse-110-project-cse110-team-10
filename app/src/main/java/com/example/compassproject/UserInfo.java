package com.example.compassproject;

import android.content.SharedPreferences;

/**
 * This class is used to setup information for user
 * Functions    UserInfo
 *              setName
 *              setUID
 *              setPrivateCode
 *              hasName
 *              getName
 *              getUID
 *              getPrivateCode
 */
public class UserInfo
{
    SharedPreferences preferences;

    /**
     * The UserInfo constructor
     * @param preferences
     */
    public UserInfo(SharedPreferences preferences)
    {
        this.preferences = preferences;
    }

    /**
     * This method is storing user name in shared preferences
     * @param name
     */
    public void setName(String name)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Name", name);
        editor.apply();
    }

    /**
     * This method is storing user's uid in shared preferences
     * @param UID
     */
    public void setUID(String UID)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("UID", UID);
        editor.apply();
    }

    /**
     * This method stores user's private code in shared preference
     * @param privateCode
     */
    public void setPrivateCode(String privateCode)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("PrivateCode", privateCode);
        editor.apply();
    }

    /**
     * This method checks if user has inputted a name
     * @return
     */
    public boolean hasName()
    {
        return !preferences.getString("Name", "").equals("");
    }

    /**
     * This method returns user name
     * @return
     */
    public String getName()
    {
        return preferences.getString("Name", "");
    }

    /**
     * This method returns user UID
     * @return
     */
    public String getUID()
    {
        return preferences.getString("UID", "");
    }

    /**
     * This method is used to ger private code
     * @return private code
     */
    public String getPrivateCode()
    {
        return preferences.getString("PrivateCode", "");
    }






}
