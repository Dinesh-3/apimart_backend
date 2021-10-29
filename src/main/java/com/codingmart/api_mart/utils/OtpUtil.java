package com.codingmart.api_mart.utils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.ThreadLocalRandom;

public class OtpUtil {

    private static final LocalTime validity = LocalTime.of(0, 5);
    public static String getOtp() {
        int random = ThreadLocalRandom.current().nextInt(10_000, 99_999 + 1);
        return Integer.toString(random);
    }

    public static boolean isValid(LocalDateTime createAt) {
        LocalDateTime validTime = createAt.plusMinutes(validity.getMinute());
        return LocalDateTime.now().isBefore(validTime);
    }

}
