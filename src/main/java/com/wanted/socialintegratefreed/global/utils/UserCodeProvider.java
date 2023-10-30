package com.wanted.socialintegratefreed.global.utils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 사용자 랜덤 코드 발급기
 */
public class UserCodeProvider {

  /**
   * 랜덤한수를 100000 ~ 1000000까지 6자리수를 뽑는다
   *
   * @return int
   */
  public static int generateAuthRandomNumber() {
    return ThreadLocalRandom.current().nextInt(100000, 1000000);
  }
}
