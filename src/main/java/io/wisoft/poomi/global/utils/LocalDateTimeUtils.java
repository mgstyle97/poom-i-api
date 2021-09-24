package io.wisoft.poomi.global.utils;

import java.time.LocalDateTime;

public class LocalDateTimeUtils {

    public static void checkChildminderActivityTime(final LocalDateTime startTime, final LocalDateTime endTime) {
        LocalDateTime currentTime = LocalDateTime.now();
        if (!currentTime.isBefore(startTime) || !currentTime.isBefore(endTime) || endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("품앗이의 활동 시간이 올바르지 않습니다.");
        }
    }

}
