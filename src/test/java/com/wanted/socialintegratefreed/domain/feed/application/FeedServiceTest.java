package com.wanted.socialintegratefreed.domain.feed.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

import com.wanted.socialintegratefreed.domain.feed.constant.FeedType;
import com.wanted.socialintegratefreed.domain.feed.dao.FeedRepository;
import com.wanted.socialintegratefreed.domain.feed.dto.request.FeedCreateRequest;
import com.wanted.socialintegratefreed.domain.feed.entity.Feed;
import com.wanted.socialintegratefreed.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FeedServiceTest {

  @InjectMocks
  private FeedService feedService;

  @Mock
  private FeedRepository feedRepository;

  private Feed mockFeed;
  private User mockUser;

  @BeforeEach
  void setUp(){
    mockUser = User.builder()
        .userId(1L)
        .email("test@example.com")
        .password("123")
        .build();

    mockFeed = Feed.builder()
        .title("제목")
        .content("내용")
        .type(FeedType.FACEBOOK)
        .user(mockUser)
        .build();
  }

  @DisplayName("게시물이 성공적으로 생성됩니다.")
  @Test
  void 게시물_생성() {
    // Given
    FeedCreateRequest request = FeedCreateRequest.builder()
        .userId(1L)
        .title("제목")
        .content("내용")
        .type(FeedType.FACEBOOK)
        .build();

    given(feedRepository.save(any(Feed.class))).willReturn(mockFeed);

    // When
    Long feedId = feedService.createFeed(request, mockUser);

    // Then
    assertThat(feedId).isEqualTo(mockFeed.getFeedId());
  }
}
