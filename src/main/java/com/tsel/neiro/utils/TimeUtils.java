package com.tsel.neiro.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

public final class TimeUtils {

    private TimeUtils() {}

    public static long getCurrentTimeInMS() {
        return LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
