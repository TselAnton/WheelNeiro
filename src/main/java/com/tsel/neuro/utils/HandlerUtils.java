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
}
