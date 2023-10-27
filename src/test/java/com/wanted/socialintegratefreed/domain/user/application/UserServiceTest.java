package com.wanted.socialintegratefreed.domain.user.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.wanted.socialintegratefreed.domain.user.dao.UserRepository;
import com.wanted.socialintegratefreed.domain.user.entity.User;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

  private User mockUser;

  @BeforeEach
  void setUp() {
    mockUser = User.builder()
        .userId(1L)
        .email("test@example.com")
        .password("123")
        .build();
  }

  @DisplayName("사용자가 정상적으로 조회된다.")
  @Test
  void 사용자_조회() {
    // Given
    given(userRepository.findById(1L)).willReturn(Optional.of(mockUser));

    // When
    User result = userService.getUserById(1L);

    // Then
    assertThat(mockUser.getUserId()).isEqualTo(result.getUserId());

  }

}
