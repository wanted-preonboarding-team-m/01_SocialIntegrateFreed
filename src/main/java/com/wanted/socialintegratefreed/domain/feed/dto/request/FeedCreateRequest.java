package com.wanted.socialintegratefreed.domain.feed.dto.request;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import com.wanted.socialintegratefreed.domain.feed.constant.FeedType;
import com.wanted.socialintegratefreed.domain.feed.entity.Feed;
import com.wanted.socialintegratefreed.domain.user.entity.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
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
@AllArgsConstructor(access = PROTECTED)
@Builder
public class FeedCreateRequest {

  @PositiveOrZero(message = "올바른 사용자 아이디를 입력해주세요.")
  @NotNull(message = "사용자 아이디를 입력해주세요.")
  private Long userId;

  @Size(max = 128)
  @NotBlank(message = "제목은 공백을 허용하지 않습니다.")
  @NotEmpty(message = "제목을 입력해주세요.")
  private String title;

  @NotEmpty(message = "내용을 입력해주세요.")
  private String content;

  @NotNull(message = "게시글 유형을 입력해주세요.")
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
