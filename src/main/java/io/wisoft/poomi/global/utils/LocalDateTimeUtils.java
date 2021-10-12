package io.wisoft.poomi.global.utils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class LocalDateTimeUtils {

    public static void checkChildCareContentActivityTime(final LocalDateTime startTime, final LocalDateTime endTime) {
        LocalDateTime currentTime = LocalDateTime.now();
        if (!currentTime.isBefore(startTime) || !currentTime.isBefore(endTime) || endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("품앗이의 활동 시간이 올바르지 않습니다.");
        }
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

}
