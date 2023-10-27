package com.wanted.socialintegratefreed.domain.user.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * 인증/인가를 거친 사용자가 accessToken 을 받기위한 dto
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UserAccessTokenDto {

    private String accessToken;

}
