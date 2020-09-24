package com.sktlab.android.base.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

public class RandomUtil {
    public static String randomString(int count) {
        return RandomStringUtils.randomAlphanumeric(count);
    }

    public static int randomInt() {
        return RandomUtils.nextInt();
    }

    public static int randomInt(int start, int end) {
        return RandomUtils.nextInt(start, end);
    }
}
