package com.example.compassproject;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DistanceCalculatorTester {
    @Test
    public void genericDistance() {
        assertEquals(0.95, DistanceCalculator.distanceBetweenCoordinates(33.015605, -117.185489, 33.001857, -117.186090), 0.01);
    }

    @Test
    public void noNegativeDistance() {
        assertEquals(918.126, DistanceCalculator.distanceBetweenCoordinates(34.863415248531176,-118.19873937500002, 47.23820759096876, -124.65870031249997), .001);
    }

    @Test
    public void zeroDistance() {
        assertEquals(0.0, DistanceCalculator.distanceBetweenCoordinates(34.863415248531176,-119.25342687499997,34.863415248531176,-119.25342687499997), 0);
    }



}
