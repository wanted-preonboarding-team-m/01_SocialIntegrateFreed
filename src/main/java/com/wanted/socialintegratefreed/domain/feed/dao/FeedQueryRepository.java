package com.wanted.socialintegratefreed.domain.feed.dao;

import com.wanted.socialintegratefreed.domain.feed.dto.request.FeedSearchCond;
import java.time.LocalDateTime;

/**
 * JpaRepository 기능을 사용하는 인터페이스와 Querydsl 기능을 사용하는 인터페이스를 분리합니다.
 * 기존의 리포지토리 인터페이스에 extends로 연결해줍니다.
 * 인터페이스명 + Impl을 이름으로 하는 구현체를 만들면 자동으로 주입됩니다. (예시: FeedQueryRepositoryImpl)
 */
public interface FeedQueryRepository {

  /**
   * 검색 조건에 부합하는 게시물의 개수를 센다.
   *
   * @param date 검색 기간
   * @param searchCond 검색 조건
   * @return 검색 조건에 부합하는 게시물의 개수
   */
  public Long search(LocalDateTime date, FeedSearchCond searchCond);
}
