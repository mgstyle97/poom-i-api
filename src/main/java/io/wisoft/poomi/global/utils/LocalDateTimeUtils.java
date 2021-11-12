package io.wisoft.poomi.global.utils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class LocalDateTimeUtils {

    public static boolean checkChildCareContentActivityTime(final LocalDateTime startTime, final LocalDateTime endTime) {
        LocalDateTime currentTime = LocalDateTime.now();

        return currentTime.isBefore(startTime) && currentTime.isBefore(endTime) && startTime.isBefore(endTime);
    }

    public static Date toDate(final LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }

    public static String getDateToString(final LocalDateTime localDateTime) {
        LocalDate date = localDateTime.toLocalDate();
        return date.toString();
    }

    public static String getTimeToString(final LocalDateTime localDateTime) {
        LocalTime time = localDateTime.toLocalTime();
        return time.toString();
    }

    public static String convertToString(final String format, final LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }

}
