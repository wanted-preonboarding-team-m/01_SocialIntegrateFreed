package com.wanted.socialintegratefreed.domain.user.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.socialintegratefreed.config.restdocs.AbstractRestDocsTests;
import com.wanted.socialintegratefreed.config.restdocs.RestDocsConfiguration;
import com.wanted.socialintegratefreed.domain.user.dto.request.UserRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(RestDocsConfiguration.class)
class UserControllerTest extends AbstractRestDocsTests {

    private static final String COMMON_URL = "/api/v1/user/sign-up";
    private UserRequestDto userRequestDto;
    private HttpServletRequest httpServletRequest;
    private MockHttpServletRequest mockHttpServletRequest;

    @BeforeEach
    void setUp() {
        userRequestDto = UserRequestDto.builder()
                .password("1234asdfgasf3")
                .email("junghun8158@naver.com")
                .build();
        mockHttpServletRequest = new MockHttpServletRequest();


    }

    @Test
    @DisplayName("회원가입 컨트롤러 테스트")
    void 회원가입_컨트롤러_테스트() throws Exception {
        //TODO
    }
}