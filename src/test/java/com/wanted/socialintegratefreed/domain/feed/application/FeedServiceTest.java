package com.wanted.socialintegratefreed.domain.feed.application;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.wanted.socialintegratefreed.domain.feed.constant.FeedType;
import com.wanted.socialintegratefreed.domain.feed.constant.SearchType;
import com.wanted.socialintegratefreed.domain.feed.constant.SearchValue;
import com.wanted.socialintegratefreed.domain.feed.dao.FeedRepository;
import com.wanted.socialintegratefreed.domain.feed.dto.request.FeedCreateRequest;
import com.wanted.socialintegratefreed.domain.feed.dto.request.FeedSearchCond;
import com.wanted.socialintegratefreed.domain.feed.dto.request.FeedUpdateRequest;
import com.wanted.socialintegratefreed.domain.feed.dto.response.FeedSearchResponse;
import com.wanted.socialintegratefreed.domain.feed.entity.Feed;
import com.wanted.socialintegratefreed.domain.user.entity.User;
import java.util.Optional;
import org.assertj.core.api.Assertions;
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
  void setUp() {
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
        .likeCount(0)
        .shareCount(0)
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

  @DisplayName("통계가 성공적으로 반환됩니다.")
  @Test
  void makeStatisticSuccess() {
    FeedSearchCond searchCond = FeedSearchCond.builder()
        .hashtag("해시태그")
        .type(SearchType.DATE)
        .start(now())
        .end(now().plusDays(7))
        .value(SearchValue.COUNT)
        .build();
    given(feedRepository.search(any(), any())).willReturn(1L);

    FeedSearchResponse response = feedService.search(searchCond);

    Assertions.assertThat(response.getSearchResult()).isNotNull();
  }

  @DisplayName("게시물 제목만 성공적으로 수정됩니다.")
  @Test
  void 게시물_제목만_수정() {
    //Given
    String originalTitle = mockFeed.getTitle();
    String originalContent = mockFeed.getContent();
    FeedType originalType = mockFeed.getType();

    FeedUpdateRequest request = FeedUpdateRequest.builder()
        .userId(mockUser.getUserId())
        .title("수정한 제목")
        .content(null)
        .type(null)
        .build();

    given(feedRepository.findById(1L)).willReturn(Optional.of(mockFeed));

    // When
    feedService.updateFeed(1L, request, mockUser);

    // Then
    assertThat(mockFeed.getTitle()).isEqualTo(request.getTitle());
    assertThat(mockFeed.getContent()).isEqualTo(originalContent);
    assertThat(mockFeed.getType()).isEqualTo(originalType);
  }

  @DisplayName("게시물 수정이 실패합니다.")
  @Test
  void 게시물_수정_실패() {
    //Given
    FeedUpdateRequest request = FeedUpdateRequest.builder()
        .userId(1L)
        .title("수정한 제목")
        .content("수정한 내용")
        .type(FeedType.THREADS)
        .build();

    given(feedRepository.findById(1L)).willReturn(Optional.of(mockFeed));

    // When & Then
    assertThatThrownBy(() -> feedService.updateFeed(2L, request, mockUser))
        .isInstanceOf(Exception.class);

    }

  @DisplayName("게시물 삭제가 실패합니다.")
  @Test
  void 게시물_삭제_실패() {
    // Given
    given(feedRepository.findById(1L)).willReturn(Optional.of(mockFeed));

    // When & Then
    assertThatThrownBy(() -> feedService.deleteFeed(2L))
        .isInstanceOf(Exception.class);
  }

  @DisplayName("게시물 좋아요수를 증가시킵니다.")
  @Test
  void 게시물_좋아요수_증가() {
    // Given
    given(feedRepository.findById(1L)).willReturn(Optional.of(mockFeed));

    // When
    feedService.like(1L);

    // Then
    assertThat(mockFeed.getLikeCount()).isEqualTo(1);
  }
}

