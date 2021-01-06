package com.helper;

public class InputHelper {

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

    public boolean isPositiveInteger(String input) {
        if (input == null) {
            return false;
        }
        int d;
        try {
            d = Integer.parseInt(input);
        } catch (NumberFormatException nfe) {
            return false;
        }
        if (d <= 0) {
            return false;
        }
        return true;
    }
}
