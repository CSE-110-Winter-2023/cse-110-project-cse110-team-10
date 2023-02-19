package com.example.compassproject;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DegreeCalculatorTester
{
    @Test
    public void genericDegree()
    {
        assertEquals(98.652, DegreeCalculator.degreeBetweenCoordinates(-12.924239968315463,-18.354989375000002, 6.473591958017654, 130.004385625 ), 0.001);
    }

    @Test
    public void noNegativeDegree()
    {
        assertEquals(340.588, DegreeCalculator.degreeBetweenCoordinates(34.863415248531176,-118.19873937500002, 47.23820759096876, -124.65870031249997 ), 0.001);
    }

    @Test
    public void zeroDegree()
    {
        assertEquals(0.0, DegreeCalculator.degreeBetweenCoordinates(34.863415248531176,-119.25342687499997, 47.68386951916552, -119.25342687499997 ), 0);
    }

    @Test
    public void fixNegativeDegree()
    {
        assertEquals(345, DegreeCalculator.fixNegative(-15), 0);
    }

    @Test
    public void ignorePositiveDegree()
    {
        assertEquals(45, DegreeCalculator.fixNegative(45), 0);
    }

    @Test
    public void phoneIsPointingNorth(){
        assertEquals(90, DegreeCalculator.rotatingToPhoneOrientation(90, 0), 0);
    }

    @Test
    public void phoneIsPointingEast(){
        assertEquals(40, DegreeCalculator.rotatingToPhoneOrientation(130, 90), 0);
    }

    @Test
    public void phoneIsRotatingPassingLocation(){
        assertEquals(330, DegreeCalculator.rotatingToPhoneOrientation(60, 90), 0);
    }
}

