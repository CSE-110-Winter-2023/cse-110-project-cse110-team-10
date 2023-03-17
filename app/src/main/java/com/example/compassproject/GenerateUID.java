package com.example.compassproject;

import com.example.compassproject.model.LocationAPI;

/**
    This class is used to generate the unique UID
    functions:  generateUID
                UIDGeneratorHelper
                checkUniqueUID
 */
public class GenerateUID
{
    /**
        This method is used to generate unique UID
        @param userName - userName to use to generate UID
        @return UID
     */
    public static String generateUID(String userName)
    {
        String UID = UIDGeneratorHelper(userName);

        while(!checkUniqueUID(UID))
        {
            UID = UIDGeneratorHelper(userName);
        }

        return UID;
    }

    /**
        This method is helper method which used to
        calculate the unique UID
        @param userName
        @return UID
     */
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

    /**
        This method is used to check if UID is unique
        @param UID
        @return true- it's unique UID
                false - it's not unique UID
     */
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
