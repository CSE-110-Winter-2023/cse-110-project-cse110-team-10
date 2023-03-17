package com.example.compassproject;

public class DistanceCalculator {
    // Calculate distance between coordinates
    public static double distanceBetweenCoordinates(double lat1, double lon1, double lat2, double lon2) {
        //convert from rad to degrees
        final double R = 3959; // Radius of the earth in miles
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // Distance in miles
        return distance;
    }
}

