package com.wanted.socialintegratefreed.domain.user.dto.request;


import com.wanted.socialintegratefreed.domain.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class UserSignUpRequestDto {

    //만들 이메일
    @Email
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;

    // 사용할 password
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?!.*(.)\\1\\1)[0-9a-zA-Z!@#$%^&*]{10,}$",
            message = "비밀번호는 최소 10자 이상이어야 하며, 3회 이상 같은 문자는 불가능 합니다.")
    private String password;


    public User toEntity(User user) {
        return User.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

}
