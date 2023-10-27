package com.wanted.socialintegratefreed.domain.user.application;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.wanted.socialintegratefreed.domain.user.dao.UserRepository;
import com.wanted.socialintegratefreed.domain.user.dto.request.UserRequestDto;
import com.wanted.socialintegratefreed.domain.user.entity.User;
import com.wanted.socialintegratefreed.global.error.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @InjectMocks
  private UserService userService;
  @Mock
  private UserRepository userRepository;

  private User user;
  private UserRequestDto userRequestDto;

  @Mock
  private BCryptPasswordEncoder passwordEncoder;


  @BeforeEach
  void setUp() {
    user = User.builder()
        .email("junghun8158@naver.com")
        .password("1234")
        .build();
    userRequestDto = UserRequestDto.builder()
        .email("test@example.com")
        .password("asdofijsdaoif234").build();
    MockitoAnnotations.openMocks(this);

  }


    @Test
    @DisplayName("사용자 등록에 성공한다.")
    void 사용자_등록에_성공한다() {

      HttpServletRequest httpServletRequest = new MockHttpServletRequest();

      //when
      int authCode = userService.signUp(userRequestDto, httpServletRequest);

      // then
      assertEquals(6, String.valueOf(authCode).length()); // 6자리 랜덤 인증 코드 확인
    }
  }


