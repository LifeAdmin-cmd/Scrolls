package com.lifeadmin.scrolls.staticFunctions;

public class Calcs {
    public static double getRandomDouble() {
        return java.lang.Math.floor(java.lang.Math.random()*(Double.MAX_VALUE-Double.MIN_VALUE+1)+Double.MIN_VALUE);
    }

    public static double getRandomRoundedDouble() {
        return Math.round(java.lang.Math.floor(java.lang.Math.random()*(Double.MAX_VALUE-Double.MIN_VALUE+1)+Double.MIN_VALUE));
    }
}
