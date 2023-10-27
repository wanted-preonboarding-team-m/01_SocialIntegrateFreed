package com.wanted.socialintegratefreed.domain.user.utils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 사용자 utils
 */
public class UserUtils {

    /**
     * 랜덤한수를 100000 ~ 1000000까지 6자리수를 뽑는다
     *
     * @return int
     */
    public static int generateAuthRandomNumber() {
        return ThreadLocalRandom.current().nextInt(100000, 1000000);
    }
}
