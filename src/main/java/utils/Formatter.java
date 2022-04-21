package utils;

import javafx.scene.control.Label;

import java.text.DecimalFormat;

/**
 * This class introduces some static methods for data formatting.
 * @author Josefinators
 */
public class Formatter {

    /**
     * Padds the given string from left.
     * @param str the string to be padded
     * @param padChar the char to with pad the string
     * @param n the length of the padded string
     * @return padded string
     */
    public static String padLeft(String str, char padChar, int n) {
        return String.format("%" + n +  "s", str).replace(' ', padChar);
    }

    /**
     * Padds the given string from right.
     * @param str the string to be padded
     * @param padChar the char to with pad the string
     * @param n the length of the padded string
     * @return padded string
     */
    public static String padRight(String str, char padChar, int n) {
        return String.format("%-" + n +  "s", str).replace(' ', padChar);
    }

    /**
     * Returns a string with #,###.00 format
     * @param amount the amount to be formatted
     * @return a string with the formatted amount
     */
    public static String twoDecimals(double amount) {
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        return formatter.format(amount);
    }
}
