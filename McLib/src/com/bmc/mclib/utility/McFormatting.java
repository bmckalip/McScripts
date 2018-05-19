package com.bmc.mclib.utility;

import java.text.DecimalFormat;

public class McFormatting {
    public static final DecimalFormat D2F = new DecimalFormat("#.##");

    public static String getElapsedTimestamp(long startTime){
        int millis = (int) (System.currentTimeMillis() - startTime);
        long second = (millis / 1000) % 60;
        long minute = (millis / (1000 * 60)) % 60;
        long hour = (millis / (1000 * 60 * 60)) % 24;
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }
}
