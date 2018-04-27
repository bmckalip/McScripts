package com.bmc.mclib.utility;

public class McCalculations {
    public static double getRobesPerHour(int count, long startTime){
        double perSecond = (double) (System.currentTimeMillis() - startTime) / 1000.0;
        return 60 * 60 * (double)(count) / perSecond;
    }
}
