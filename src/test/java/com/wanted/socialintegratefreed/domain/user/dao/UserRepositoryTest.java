package com.wanted.socialintegratefreed.domain.user.dao;

import com.wanted.socialintegratefreed.domain.user.constant.UserEnable;
import com.wanted.socialintegratefreed.domain.user.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

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
}
