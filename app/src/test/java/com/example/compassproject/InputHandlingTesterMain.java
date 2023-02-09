package com.example.compassproject;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class InputHandlingTesterMain {
    @Test
    public void testEmptyInputForLabel(){
        assertEquals(true, InputHandling.isLabelEmpty(""));
    }

    @Test
    public void testNotEmptyInputForLabel(){
        assertEquals(false, InputHandling.isLabelEmpty("Julia's house"));
    }

    @Test
    public void testEmptyInputForCoordinate(){
        assertEquals(true, InputHandling.isCoordinatesEmpty(""));
    }

    @Test
    public void testNonEmptyInputForCoordinate(){
        assertEquals(false, InputHandling.isCoordinatesEmpty("777, 999"));
    }

    /* Regex Explanation:
         0/1 minus sign, followed by 1+ digits, followed by period, followed by 1+ digits,
         followed by any amount of whitespace, followed by comma, followed by any amount of whitespace,
         0/1 minus sign, followed by 1+ digits, followed by period, followed by 1+ digits
          */
    /*
     * 1st negative at the beginning
     * 2nd negative at the end
     * 3rd alphabetic string in longitude/latitude
     * 4th only 1 coordinate begin enter by user
     * 5th test if customer don't enter comma to separate the longitude and latitude.
     * 6th test if the space is not followed after comma.
     * 7th test if user only enter whole number
     * */
    @Test
    public void testNegativeLatitude(){
        assertEquals(true,InputHandling.isCoordinatesValid("-32.879, 117.236"));
    }

    @Test
    public void testNegativeLongitude(){
        assertEquals(true,InputHandling.isCoordinatesValid("32.879, -117.236"));
    }

    @Test
    public void testAlphabeticStringInCoordinate(){
        assertEquals(false,InputHandling.isCoordinatesValid("Julia's house"));
    }

    @Test
    public void testInputWithOnlyOneCoordinate(){
        assertEquals(false,InputHandling.isCoordinatesValid("-117.236"));
    }

    @Test
    public void testIfNoCommaBetweenLatitudeAndLongitude(){
        assertEquals(false,InputHandling.isCoordinatesValid("32.879 -117.236"));
    }

    @Test
    public void testIfNoSpaceFollowedAfterComma(){
        assertEquals(true,InputHandling.isCoordinatesValid("32.879,-117.236"));
    }

    @Test
    public void testIfUserEnterWholeNumberForCoordinate(){
        assertEquals(false,InputHandling.isCoordinatesValid("32, -117"));
    }
}