package com.wanted.socialintegratefreed.domain.feed.dto.request;

import static lombok.AccessLevel.PRIVATE;

import com.wanted.socialintegratefreed.domain.feed.constant.FeedType;
import com.wanted.socialintegratefreed.domain.feed.entity.Feed;
import com.wanted.socialintegratefreed.domain.user.entity.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 새로운 게시글 생성 요청 dto
 */
@Getter
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
@Builder
public class FeedCreateRequest {

  @NotNull
  private Long userId;

  @NotEmpty
  private String title;

  @NotEmpty
  private String content;

  @NotNull
  private FeedType type;

  /**
   * DTO 객체를 Feed 엔터티로 변환
   *
   * @param user
   * @return Feed 엔티티 객체
   */
  public Feed toEntity(User user) {
    return Feed.builder()
        .title(this.title)
        .content(this.content)
        .viewCount(0)
        .likeCount(0)
        .shareCount(0)
        .type(this.type)
        .user(user)
        .build();
  }
}
