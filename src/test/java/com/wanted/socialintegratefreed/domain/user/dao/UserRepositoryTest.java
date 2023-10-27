package com.wanted.socialintegratefreed.domain.user.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.wanted.socialintegratefreed.domain.user.entity.User;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @AfterEach
  void cleanUp() {
    userRepository.deleteAll();
  }

  @DisplayName("사용자가 정상적으로 조회된다.")
  @Test
  void 사용자_조회() {
    // Given
    String email = "test@example.com";
    String password = "password123";
    User user = User.builder()
        .email(email)
        .password(password)
        .build();

    //When
    User savedUser = userRepository.save(user);

    // Then
    userRepository.findById(savedUser.getUserId())
            .orElseThrow(() -> new NoSuchElementException());
  }

}