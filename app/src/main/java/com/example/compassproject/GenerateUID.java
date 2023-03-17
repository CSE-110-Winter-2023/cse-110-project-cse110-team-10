package com.example.compassproject;

import com.example.compassproject.model.LocationAPI;

public class GenerateUID
{
    //generates unique UID
    public static String generateUID(String userName)
    {
        String UID = UIDGeneratorHelper(userName);

        while(!checkUniqueUID(UID))
        {
            UID = UIDGeneratorHelper(userName);
        }

        return UID;
    }

    //generates a UID
    public static String UIDGeneratorHelper(String userName)
    {
        String UID = userName + "-";
        for (int i = 0; i < 5; i++)
        {
            int randNumber = (int) (Math.random() * 10);
            UID = UID + randNumber;
        }

        return UID;
    }



    //checks if UID inputted is unique
    public static boolean checkUniqueUID(String UID)
    {
        LocationAPI singleton = LocationAPI.provide();

        if(singleton.getLocationAsync(UID) == null){
            return true;
        }
        else {
            return false;
        }
    }


}
