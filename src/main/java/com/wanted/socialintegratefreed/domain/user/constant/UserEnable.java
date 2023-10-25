package com.wanted.socialintegratefreed.domain.user.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
/**
 * UserEnable: 로그인 여부
 */
public enum UserEnable {
    /**
     * @USER_ENABLED : 가입 여부 활성화
     * @USER_DISABLED : 가입 여부 비활성화
     */
    USER_ENABLED("활성화"),
    USER_DISABLED("비활성화");
    private String enable;

}
