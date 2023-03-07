package com.example.compassproject;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DistanceCalculatorTester {
    @Test
    public void genericDistance() {
        assertEquals(0.95, DistanceCalculator.distanceBetweenCoordinates(33.015605, -117.185489, 33.001857, -117.186090), 0.01);
    }
}

