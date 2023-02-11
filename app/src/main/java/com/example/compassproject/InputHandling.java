/*
InputHandling checks if user inputted data is formatted correctly.
 */
package com.example.compassproject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputHandling {
    // Return true if both the coordinates and label are valid
    public static boolean isInputValid(String coords, String label){
        return getInputError(coords, label) == 0;
    }

    // Returns error message based on error code
    public static String getErrorMessage(int errorCode){
        if(errorCode == 1){
            return "Please make sure the coordinates are not empty.";
        }
        else if(errorCode == 2){
            return "Please make sure the coordinates are validly formatted.";
        }
        else{ // errorCode == 3
            return "Please make sure the label is not empty.";
        }
    }

    // Checks if entire input is valid
    /*
    0 - input is valid
    1 - coordinates are empty
    2 - coordinates are invalid
    3 - label is empty
     */
    public static int getInputError(String coords, String label){
        if(isCoordinatesEmpty(coords)){
            return 1;
        }
        if(!isCoordinatesValid(coords)){
            return 2;
        }
        if(isLabelEmpty(label)){
            return 3;
        }

        return 0;
    }

    // Checks if coordinates are empty
    public static boolean isCoordinatesEmpty(String coords){
        return isStringEmpty(coords);
    }

    // Checks if coordinates match the standard formatting
    public static boolean isCoordinatesValid(String coords) {
        /* Regex Explanation:
        0/1 minus sign, followed by 1+ digits, followed by period, followed by 1+ digits,
        followed by any amount of whitespace, followed by comma, followed by any amount of whitespace,
        0/1 minus sign, followed by 1+ digits, followed by period, followed by 1+ digits
         */
        Pattern p = Pattern.compile("-{0,1}\\d+\\.\\d+\\s*,\\s*-{0,1}\\d+\\.\\d+");
        Matcher m = p.matcher(coords);
        return m.matches();
    }

    // Checks if label is empty
    public static boolean isLabelEmpty(String label){
        return isStringEmpty(label);
    }

    // Checks if string is empty
    private static boolean isStringEmpty(String str){
        return str.equals("");
    }

}