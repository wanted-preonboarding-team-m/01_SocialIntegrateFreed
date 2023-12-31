package com.wanted.socialintegratefreed.domain.user.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.socialintegratefreed.config.restdocs.AbstractRestDocsTests;
import com.wanted.socialintegratefreed.domain.user.annotation.WithMockCustomUser;
import com.wanted.socialintegratefreed.domain.user.application.UserService;
import com.wanted.socialintegratefreed.domain.user.dto.request.UserRequestAuthCodeDto;
import com.wanted.socialintegratefreed.domain.user.dto.request.UserSignUpRequestDto;
import com.wanted.socialintegratefreed.global.config.security.jwt.CustomAccessDeniedHandler;
import com.wanted.socialintegratefreed.global.config.security.jwt.JwtTokenProvider;
import com.wanted.socialintegratefreed.global.config.security.web.WebSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(UserController.class)
@Import(WebSecurityConfig.class)
class UserControllerTest extends AbstractRestDocsTests {

  private static final String COMMON_URL = "/api/v1/user";
  private static final String FEED_URL = "/api/v1/feeds/**";

  @MockBean
  private JwtTokenProvider jwtTokenProvider;

  @MockBean
  private CustomAccessDeniedHandler accessDeniedHandler;
  @MockBean
  private UserService userService;

  @Autowired
  private final ObjectMapper objectMapper = new ObjectMapper();

  private UserSignUpRequestDto userSignUpRequestDto;

  @BeforeEach
  void setUp() {
    userSignUpRequestDto = UserSignUpRequestDto.builder()
        .password("asdf2cvzcv4t")
        .email("zxcvzxccccccc@naver.com")
        .build();

  }

  @Test
  @DisplayName("회원가입 컨트롤러 테스트")
  void 회원가입_컨트롤러_테스트() throws Exception {
    //TODO
    mockMvc.perform(MockMvcRequestBuilders.post(COMMON_URL + "/sign-up")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userSignUpRequestDto)))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @DisplayName("로그인 컨트롤러 테스트")
  void 로그인_컨트롤러_테스트() throws Exception {
    //TODO
    mockMvc.perform(MockMvcRequestBuilders.post(COMMON_URL + "/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(userSignUpRequestDto)))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @DisplayName("인증번호 컨트롤러 테스트")
  void 인증번호_컨트롤러() throws Exception {
    //TODO
    UserRequestAuthCodeDto code = UserRequestAuthCodeDto.builder()
        .code(232543)
        .build();
    mockMvc.perform(MockMvcRequestBuilders.post(COMMON_URL + "/verify-user-code")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(code)))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void 인증된_사용자가_아닌경우_401에러() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(FEED_URL)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @WithMockCustomUser
  void 인증된_사용자인_경우_200() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(FEED_URL)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
  }


}