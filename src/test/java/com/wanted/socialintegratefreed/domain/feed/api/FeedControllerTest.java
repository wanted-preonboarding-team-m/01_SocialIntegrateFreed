package com.wanted.socialintegratefreed.domain.feed.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.socialintegratefreed.config.restdocs.AbstractRestDocsTests;
import com.wanted.socialintegratefreed.domain.feed.application.FeedService;
import com.wanted.socialintegratefreed.domain.feed.constant.FeedType;
import com.wanted.socialintegratefreed.domain.feed.dto.request.FeedCreateRequest;
import com.wanted.socialintegratefreed.domain.feed.dto.request.FeedUpdateRequest;
import com.wanted.socialintegratefreed.domain.feed.entity.Feed;
import com.wanted.socialintegratefreed.domain.user.application.UserService;

import com.wanted.socialintegratefreed.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import static com.wanted.socialintegratefreed.domain.feed.constant.FeedType.INSTAGRAM;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
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

  private User mockUser;
  private Feed mockFeed;

  @BeforeEach
  public void setUp(){
    mockMvc = MockMvcBuilders
        .webAppContextSetup(context)
        .apply(SecurityMockMvcConfigurers.springSecurity()) // 스프링 시큐리티 설정 적용
        .defaultRequest(post("/**").with(csrf()))
        .defaultRequest(patch("/**").with(csrf()))
        .defaultRequest(delete("/**").with(csrf()))
        .build();
  }


  @DisplayName("게시물 생성 요청 api가 성공한다.")
  @Test
  @WithMockUser(roles = {"USER"})
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
    mockMvc.perform(post("/api/v1/feeds")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isCreated());
  }

  @DisplayName("게시물 생성 요청 api가 실패한다.")
  @Test
  @WithMockUser(roles = {"USER"})
  public void 게시물_생성_실패() throws Exception {
    // Given
    FeedCreateRequest request = FeedCreateRequest.builder()
        .userId(null)
        .title(null)
        .content(null)
        .type(FeedType.FACEBOOK)
        .build();

    String requestJson = objectMapper.writeValueAsString(request);

    // When & Then
    mockMvc.perform(post("/api/v1/feeds")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().is4xxClientError());
  }

  @DisplayName("게시물 수정 요청 api가 성공한다.")
  @Test
  @WithMockUser(roles = {"USER"})
  public void 게시물_수정() throws Exception {
    Long feedId = 1L;
    FeedUpdateRequest request = FeedUpdateRequest.builder()
        .userId(1L)
        .title("수정 제목")
        .content("수정 내용")
        .type(INSTAGRAM)
        .build();

    mockMvc.perform(put("/api/v1/feeds/" + feedId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
  }

  @DisplayName("게시물 수정 요청 api가 실패한다.")
  @Test
  @WithMockUser(roles = {"USER"})
  public void 게시물_수정_실패() throws Exception {
    Long feedId = 1L;
    FeedUpdateRequest request = FeedUpdateRequest.builder()
        .userId(null)
        .title(null)
        .content("수정 내용")
        .build();

    mockMvc.perform(put("/api/v1/feeds/" + feedId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().is4xxClientError());
  }

  @DisplayName("게시물 삭제 요청 api가 성공한다.")
  @Test
  @WithMockUser(roles = {"USER"})
  public void 게시물_삭제() throws Exception {
    Long feedId = 1L;

    mockMvc.perform(delete("/api/v1/feeds/" + feedId))
        .andExpect(status().isOk());
  }

  @DisplayName("게시물 조회 요청 api가 성공한다.")
  @Test
  @WithMockUser(roles = {"USER"})
  public void 게시물_상세_조회() throws Exception {
    Long feedId = 1L;

    mockMvc.perform(get("/api/v1/feeds/" + feedId))
        .andExpect(status().isOk());
  }

}
