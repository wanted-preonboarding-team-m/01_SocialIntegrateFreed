package com.wanted.socialintegratefreed.domain.user.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * 인증코드 체킹을 위한 dto
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
public class UserRequestAuthCodeDto {

    // 회원가입 할때 받았던 코드
    private int code;

    // email이 세션에 있는지 확인하기위한
    private String verifierEmail;


}
