package com.tsel.neiro.utils;

public final class HandlerUtils {

    private HandlerUtils() {}

    public static String getColorName(Integer val) {
        switch (val) {
            case 0:
                return "BLACK";
            case 1:
                return "RED";
            case 2:
                return "BLUE";
            case 3:
                return "GOLD";
            default:
                return null;
        }
    }
}
