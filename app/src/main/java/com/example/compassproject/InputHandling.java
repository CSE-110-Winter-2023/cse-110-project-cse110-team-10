/*
InputHandling checks if user inputted data is formatted correctly.
 */
package com.example.compassproject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputHandling {
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