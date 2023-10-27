package com.wanted.socialintegratefreed.domain.feed.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.socialintegratefreed.config.restdocs.AbstractRestDocsTests;
import com.wanted.socialintegratefreed.domain.feed.application.FeedService;
import com.wanted.socialintegratefreed.domain.feed.constant.FeedType;
import com.wanted.socialintegratefreed.domain.feed.dto.request.FeedCreateRequest;
import com.wanted.socialintegratefreed.domain.feed.entity.Feed;
import com.wanted.socialintegratefreed.domain.user.application.UserService;

import com.wanted.socialintegratefreed.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.web.context.WebApplicationContext;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FeedController.class)
public class FeedControllerTest extends AbstractRestDocsTests {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext context;

  @MockBean
  private FeedService feedService;

  @MockBean
  private UserService userService;

  private final ObjectMapper objectMapper = new ObjectMapper();


  @DisplayName("게시물 생성 요청 api가 성공한다.")
  @Test
  @WithMockUser(roles = {"USER"}) //401 Unauthorized 방지를 위한 권한 부여
  public void 게시물_생성() throws Exception {
    // Given
    FeedCreateRequest request = FeedCreateRequest.builder()
        .userId(1L)
        .title("제목")
        .content("내용")
        .type(FeedType.FACEBOOK)
        .build();

    String requestJson = objectMapper.writeValueAsString(request);

    // When & Then
    mockMvc.perform(post("/feeds")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .with(csrf())) //403 Forbidden 방지를 위한 CSRF 토큰 제공
        .andExpect(status().isCreated());
  }
}
