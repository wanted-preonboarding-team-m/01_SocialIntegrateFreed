package com.wanted.socialintegratefreed.domain.user.dto;


import com.wanted.socialintegratefreed.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UserSaveRequestDto {

    private String email;
    private String password;


    public User toEntity(User user) {
        return User.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

}
