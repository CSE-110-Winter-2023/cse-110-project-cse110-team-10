package com.example.compassproject;

public class DegreeCalculator {

    // Calculate bearing between two coordinates
    // First latitude and longitude pair must be user's coordinates
    public static float degreeBetweenCoordinates(double latitude_1, double longitude_1, double latitude_2, double longitude_2)
    {
        double longitude_difference = (longitude_2-longitude_1) * (Math.PI/180);
        latitude_1 = latitude_1 * (Math.PI/180);
        latitude_2 = latitude_2 * (Math.PI/180);

        double x = Math.cos(latitude_2) * Math.sin(longitude_difference);

        double y = (Math.cos(latitude_1) * Math.sin(latitude_2)) - (Math.sin(latitude_1)
                * Math.cos(latitude_2) * Math.cos(longitude_difference));

        double angle = Math.atan2(x,y);

        float degree = (float) (angle * (180/Math.PI));

        degree = fixNegative(degree);

        return degree;
    }

    // Standardize angle to be in [0,360)
    public static float fixNegative(float degree)
    {
        if(degree < 0)
        {
            degree = 360+degree;
        }

        return degree;
    }

    // Recalculate angle to adjust for phone's orientation
    public static float rotatingToPhoneOrientation(float originalAngle, float phoneOrientation){
        return fixNegative(originalAngle - phoneOrientation);
    }
}
