package com.wanted.socialintegratefreed.domain.user.dto.request;


import com.wanted.socialintegratefreed.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UserRequestDto {

    private String email;
    private String password;


    public User toEntity(User user) {
        return User.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

}
