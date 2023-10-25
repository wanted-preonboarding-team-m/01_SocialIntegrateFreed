package com.wanted.socialintegratefreed.domain.user.application;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.wanted.socialintegratefreed.domain.user.dao.UserRepository;
import com.wanted.socialintegratefreed.domain.user.dto.UserSaveRequestDto;
import com.wanted.socialintegratefreed.domain.user.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    private User user;
    private UserSaveRequestDto userSaveRequestDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("junghun8158@naver.com")
                .password("1234")
                .build();

    }

    @Nested
    @DisplayName("사용자 등록 테스트")
    class UserCreate {

        @Test
        @DisplayName("사용자 등록에 성공한다.")
        void 사용자_등록에_성공한다() {

            UserSaveRequestDto saveUserRequestDto = UserSaveRequestDto.builder()
                    .password(user.getPassword())
                    .email(user.getEmail())
                    .build();

            given(userRepository.save(any(User.class))).willReturn(user);

            User createdUser = userService.createUser(saveUserRequestDto);

            Assertions.assertEquals(createdUser, user);

        }
    }
}
