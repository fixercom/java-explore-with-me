package ru.practicum.ewm.util;

import java.time.LocalDateTime;

public class DateUtils {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String START_TIME_FOR_STATS = "2000-01-01 00:00:00";
    public static final String END_TIME_FOR_STATS = "2100-01-01 00:00:00";

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

}
