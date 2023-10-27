package com.wanted.socialintegratefreed.domain.user.dao;


import com.wanted.socialintegratefreed.domain.user.entity.User;
import org.junit.jupiter.api.Assertions;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 현재 로컬 디비를 사용하고 있기 때문에
public class UserRepositoryTest {

    private User user;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("junghun8158@naver.com")
                .password("1234")
                .build();
    }


    @Test
    @DisplayName("회원 등록에 정상적으로 성공한다 ")
    void 회원_등록에_성공() {
        User newUser = userRepository.save(user);
        Assertions.assertEquals(user.getEmail(), newUser.getEmail());
        Assertions.assertEquals(user.getPassword(), newUser.getPassword());
        Assertions.assertEquals(user, newUser);
    }

  @DisplayName("사용자가 정상적으로 조회된다.")
  @Test
  void 사용자_조회() {

    //When
    User savedUser = userRepository.save(user);

    // Then
    userRepository.findById(savedUser.getUserId())
        .orElseThrow(() -> new NoSuchElementException());
  }
}

