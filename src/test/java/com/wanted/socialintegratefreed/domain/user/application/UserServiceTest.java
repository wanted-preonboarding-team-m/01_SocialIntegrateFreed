package com.wanted.socialintegratefreed.domain.user.application;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wanted.socialintegratefreed.domain.feed.entity.Feed;
import com.wanted.socialintegratefreed.domain.user.constant.UserEnable;
import com.wanted.socialintegratefreed.domain.user.dao.UserRepository;
import com.wanted.socialintegratefreed.domain.user.dto.request.UserSignUpRequestDto;
import com.wanted.socialintegratefreed.domain.user.dto.response.UserAccessTokenDto;
import com.wanted.socialintegratefreed.domain.user.entity.User;
import com.wanted.socialintegratefreed.domain.user.jwt.JwtTokenProvider;
import com.wanted.socialintegratefreed.global.error.BusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.Assertions;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static final String TEST_EMAIL = "test@example.com";
    private static final String SECRET_KEY = "socialIntegrateFreedwantedMsocialIntegrateFreedapplication2023secret";

    private static final long EXPIRES = 3600000;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    private User user;
    private UserSignUpRequestDto userRequestDto;
    @Mock
    private HttpServletRequest httpServletRequest;


    @Mock
    private HttpSession httpSession;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;


    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("junghun8158@naver.com")
                .password("1234ereq")
                .build();
        userRequestDto = UserSignUpRequestDto.builder()
                .email("junghun8158@naver.com")
                .password("1234ereq").build();
        MockitoAnnotations.openMocks(this);

    }

    @Nested
    @DisplayName("사용자 등록 테스트")
    class UserCreate {

        @Test
        @DisplayName("사용자 등록에 성공한다.")
        void 사용자_등록에_성공한다() {

            HttpServletRequest httpServletRequest = new MockHttpServletRequest();

            //when
            int authCode = userService.signUp(userRequestDto, httpServletRequest);

            // then
            assertEquals(6, String.valueOf(authCode).length()); // 6자리 랜덤 인증 코드 확인
        }

        @Test
        @DisplayName("사용자 세션 저장소에 저장되면서 60초 이후 삭제 된다.")
        void 사용자_세션_저장소에_저장되면서_60초_이후_데이터가_삭제_된다() throws InterruptedException {
            //given
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("email", "john@example.com");
            when(httpServletRequest.getSession()).thenReturn(httpSession);

            int MAX_INACTIVATE_INTERVAL = 60;

            //when
            userService.storeUserInSession(userMap, httpServletRequest);

            verify(httpSession).setAttribute(eq("user"), eq(userMap));
            verify(httpSession).setMaxInactiveInterval(eq(MAX_INACTIVATE_INTERVAL));

            Thread.sleep(60000); // 60초 대기

            //then
            Object sessionData = httpSession.getAttribute("user");
            Assertions.assertNull(sessionData);
        }
    }

    @Nested
    @DisplayName("jwt 토큰 테스트")
    class jwtCreateTest {


        @Test
        @DisplayName("JWT 를 파싱하여 정확히 값이 들어가있는지 확인 후 현재날짜로부터 유효한지 확인한다.")
        public void JWT를_파싱하여_정확히_값이_들어가있는지_확인_후_현재날짜로부터_유효한지_확인한다() {
            SecretKey secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
            Date expiration = new Date(System.currentTimeMillis() + EXPIRES);

            String jwt = Jwts.builder()
                    .setHeaderParam("type", "jwt")
                    .claim("userEmail", TEST_EMAIL)
                    .setIssuedAt(new Date())
                    .setExpiration(expiration)
                    .signWith(SignatureAlgorithm.HS256, secretKey)
                    .compact();
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
            //then
            assertEquals(TEST_EMAIL, claims.get("userEmail"));
            // 만료 시간 확인
            Date expirationd = claims.getExpiration();
            Date now = new Date();
            // 현재 시간 이후로 유효한지 확인
            assertEquals(true, expirationd.after(now));
        }
    }

    @Nested
    @DisplayName("사용자 로그인 테스트")
    class userLoginTest {


        @BeforeEach
        void setUp() {
            userService = new UserService(userRepository, passwordEncoder, jwtTokenProvider);

        }


        @Test
        @DisplayName("사용자가 로그인에 성공하면 jwt를 발급한다.")
        public void 사용자가_로그인_성공시_jwt를_발급한다() {

            when(userRepository.findByEmail(userRequestDto.getEmail())).thenReturn(Optional.of(user));
            when(jwtTokenProvider.createJwt(user.getEmail())).thenReturn("fake_jwt_token");
            when(passwordEncoder.matches(user.getPassword(), userRequestDto.getPassword())).thenReturn(true);

            // 서비스 메서드 호출
            UserAccessTokenDto userAccessTokenDto = userService.login(userRequestDto);

            // 검증: 반환된 DTO에서 JWT 토큰이 예상대로 생성되었는지 확인
            assertEquals("fake_jwt_token", userAccessTokenDto.getAccessToken());

        }


    }

}
