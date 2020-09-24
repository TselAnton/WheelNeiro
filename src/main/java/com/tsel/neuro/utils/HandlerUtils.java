package com.tsel.neuro.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class HandlerUtils {

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

    public static Double getNormalizeNum(Integer val) {
        switch (val) {
            case 0:
                return 0.25;
            case 1:
                return 0.50;
            case 2:
                return 0.75;
            case 3:
                return 1.0;
            default:
                return 0.0;
        }
    }
}
