package com.example.compassproject;

import static org.junit.Assert.assertEquals;

import android.renderscript.ScriptGroup;

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

    /*
      1) Coordinate empty, label empty
      2) Coordinate incorrect, label empty
      3) Coordinate correct, label empty
      4) Coordinate empty, label filled
      5) Coordinate incorrect, label filled
      6) Coordinate correct, label filled
     */

    @Test
    public void testIfCoordinateEmptyAndLabelEmpty(){
        assertEquals(1, InputHandling.getInputError("", ""));
    }

    @Test
    public void testIfCoordinateInvalidAndLabelEmpty(){
        assertEquals(2, InputHandling.getInputError("abc", ""));
    }

    @Test
    public void testIfCoordinateValidAndLabelEmpty(){
        assertEquals(3, InputHandling.getInputError("32.879, -117.236", ""));
    }

    @Test
    public void testIfCoordinateEmptyAndLabelValid(){
        assertEquals(1, InputHandling.getInputError("", "Julia's house"));
    }

    @Test
    public void testIfCoordinateInvalidAndLabelValid(){
        assertEquals(2, InputHandling.getInputError("abc", "Julia's house"));
    }

    @Test
    public void testIfCoordinateValidAndLabelValid(){
        assertEquals(0, InputHandling.getInputError("32.879, -117.236", "Julia's house"));
    }

    @Test
    public void testErrorMessage1(){
        assertEquals("Please make sure the coordinates are not empty.", InputHandling.getErrorMessage(1));
    }

    @Test
    public void testErrorMessage2(){
        assertEquals("Please make sure the coordinates are validly formatted.", InputHandling.getErrorMessage(2));
    }

    @Test
    public void testErrorMessage3(){
        assertEquals("Please make sure the label is not empty.", InputHandling.getErrorMessage(3));
    }

    @Test
    public void testInvalidInput(){
        assertEquals(false, InputHandling.isInputValid("", ""));
    }

    @Test
    public void testValidInput(){
        assertEquals(true, InputHandling.isInputValid("32.879, -117.236", "Julia's house"));
    }
}