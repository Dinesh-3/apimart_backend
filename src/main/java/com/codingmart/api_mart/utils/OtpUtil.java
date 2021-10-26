package com.codingmart.api_mart.utils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class OtpUtil {

//    private static TimeUnit unit = TimeUnit.MINUTES;
    private static LocalTime validity = LocalTime.of(0, 5);
    public static String getOtp() {
        int random = ThreadLocalRandom.current().nextInt(10_000, 99_999 + 1);
        String otp = Integer.toString(random);
        return otp;
    }

    public static boolean isValid(LocalDateTime createAt) {
//        createAt = LocalDateTime.of(2021, 10, 20, 21, 20);
        LocalDateTime validTime = createAt.plusMinutes(validity.getMinute());
        boolean result = LocalDateTime.now().isBefore(validTime);
        return result;
    }

}
