package com.wanted.socialintegratefreed.domain.feed.dto.request;

import static lombok.AccessLevel.PRIVATE;

import com.wanted.socialintegratefreed.domain.feed.constant.FeedType;
import com.wanted.socialintegratefreed.domain.feed.entity.Feed;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *  게시글 수정 요청 dto
 */
@Getter
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
@Builder
public class FeedUpdateRequest {
  @NotNull
  private Long userId;

  private String title;

  private String content;

  private FeedType type;

  /**
   * DTO 객체를 Feed 엔터티로 변환
   *
   * @param feed
   * @return Feed 엔티티 객체
   */
  public Feed toEntity(Feed feed) {
    return Feed.builder()
        .title(this.title)
        .content(this.content)
        .type(this.type)
        .build();
  }
}
