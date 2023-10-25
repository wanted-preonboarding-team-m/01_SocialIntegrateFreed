package com.wanted.socialintegratefreed.domain.user.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserEnable {
    USER_ENABLED("활성화"),
    USER_DISABLED("비활성화");
    private String enable;


}
