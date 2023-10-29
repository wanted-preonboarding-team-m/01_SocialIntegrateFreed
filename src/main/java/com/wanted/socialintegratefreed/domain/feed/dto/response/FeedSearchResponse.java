package com.wanted.socialintegratefreed.domain.feed.dto.response;

import java.util.List;
import lombok.Getter;

/**
 * FeedSearchCond를 조건으로 통계를 낸 결과를 담는 DTO
 */
@Getter
public class FeedSearchResponse {

  // 검색 조건에 부합하는 통계 결과 리스트
  private final List<String> searchResult;

  public FeedSearchResponse(List<String> searchResult) {
    this.searchResult = searchResult;
  }
}
