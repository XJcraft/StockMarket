package com.sunyard.util;

/**
 * Created by Weiyuan on 2016/1/27.
 */
public class InfoUtil {
    public static String removeColor(String str) {
        if (str == null)
            return null;
        str = str.replaceAll("(?i)&[0-F]", "");
        return str;
    }

    public static boolean matchPattern(String input, String line) {
        return removeColor(line).equalsIgnoreCase(input);
    }

    public static String average(int itemP, int moneyP) {
        if (itemP > moneyP) {
            double price = (double) itemP / (double) moneyP;
            return String.format("%.3f", price) + ":1";
        } else {
            double price = (double) moneyP / (double) itemP;
            return "1:" + String.format("%.3f", price);
        }
    }
}
