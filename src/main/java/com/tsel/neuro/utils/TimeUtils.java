package com.tsel.neuro.utils;

import static java.time.Instant.ofEpochMilli;
import static java.util.Optional.ofNullable;

import java.time.LocalDateTime;
import java.time.ZoneId;

public final class TimeUtils {

    private TimeUtils() {}

    public static long getCurrentTimeInLong() {
        return LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static LocalDateTime convertLongToLocalDateTime(Long milliseconds) {
        return ofNullable(milliseconds)
                .map(m -> ofEpochMilli(m).atZone(ZoneId.systemDefault()).toLocalDateTime())
                .orElse(null);
    }
}
