package com.helper;

public class InputChecker {

    public boolean isFloat(String input) {
        if (input == null) {
            return false;
        }
        try {
            float d = Float.parseFloat(input);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public boolean isInteger(String input) {
        if (input == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(input);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
