package com.wanted.socialintegratefreed.domain.feed.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

import com.wanted.socialintegratefreed.domain.feed.constant.FeedType;
import com.wanted.socialintegratefreed.domain.feed.dao.FeedRepository;
import com.wanted.socialintegratefreed.domain.feed.dto.request.FeedCreateRequest;
import com.wanted.socialintegratefreed.domain.feed.dto.request.FeedUpdateRequest;
import com.wanted.socialintegratefreed.domain.feed.entity.Feed;
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


  @DisplayName("게시물이 성공적으로 수정됩니다.")
  @Test
  void 게시물_수정() {
    //Given
    FeedUpdateRequest request = FeedUpdateRequest.builder()
        .userId(1L)
        .title("수정한 제목")
        .content("수정한 내용")
        .type(FeedType.THREADS)
        .build();

    given(feedRepository.findById(1L)).willReturn(Optional.of(mockFeed));

    // When
    feedService.updateFeed(1L, request, mockUser);

    // Then
    assertThat(mockFeed.getTitle()).isEqualTo(request.getTitle());
    assertThat(mockFeed.getContent()).isEqualTo(request.getContent());
    assertThat(mockFeed.getType()).isEqualTo(request.getType());
  }

  @DisplayName("게시물이 성공적으로 삭제됩니다.")
  @Test
  void 게시물_삭제() {
    // Given
    given(feedRepository.findById(1L)).willReturn(Optional.of(mockFeed));

    // When
    feedService.deleteFeed(1L);

    // Then
    then(feedRepository).should().deleteById(1L);
  }
}
