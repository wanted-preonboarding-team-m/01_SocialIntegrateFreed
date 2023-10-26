package com.wanted.socialintegratefreed.domain.user.dto.request;


import com.wanted.socialintegratefreed.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 회원가입 할때 사용하는 dto
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UserRequestDto {

    //만들 이메일
    private String email;

    // 사용할 password
    private String password;


    public User toEntity(User user) {
        return User.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

}
