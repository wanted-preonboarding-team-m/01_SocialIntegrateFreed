package com.wanted.socialintegratefreed.domain.feed.dto.request;

import com.wanted.socialintegratefreed.domain.feed.constant.SearchType;
import com.wanted.socialintegratefreed.domain.feed.constant.SearchValue;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

/**
 * 컨트롤러에서 쿼리 파라미터를 FeedSearchCond 객체로 변환하여 통계 조건으로 사용합니다.
 */
@Getter
public class FeedSearchCond {

  // 통계의 타겟이 되는 해시태그명
  private final String hashtag;

  /**
   * DATE: start ~ end 기간내 일자별로 통계
   * HOUR: start ~ end 기간내 시간별로 통계
   */
  private final SearchType type;

  // 조회 기준 시작일
  private final LocalDateTime start;

  // 조회 기준 종료일
  private final LocalDateTime end;

  /**
   * 게시물의 어떤 속성을 통계 낼지 결정
   * count: 게시물 개수,
   * view_count: 조회된 게시물들의 view_count 합
   * like_count: 조회된 게시물들의 like_count 합
   * share_count: 조회된 게시물들의 share_count 합
   */
  private final SearchValue value;

  @Builder
  public FeedSearchCond(String hashtag, SearchType type, LocalDateTime start, LocalDateTime end, SearchValue value) {
    this.hashtag = hashtag;
    this.type = type;
    this.start = start;
    this.end = end;
    this.value = value;
  }
}
