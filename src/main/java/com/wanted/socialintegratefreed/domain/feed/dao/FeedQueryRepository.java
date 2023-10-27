package com.wanted.socialintegratefreed.domain.feed.dao;

import com.wanted.socialintegratefreed.domain.feed.dto.request.FeedSearchCond;
import com.wanted.socialintegratefreed.domain.feed.entity.Feed;
import java.util.List;

/**
 * JpaRepository 기능을 사용하는 인터페이스와 Querydsl 기능을 사용하는 인터페이스를 분리합니다.
 * 기존의 리포지토리 인터페이스에 extends로 연결해줍니다.
 * 인터페이스명 + Impl을 이름으로 하는 구현체를 만들면 자동으로 주입됩니다. (예시: FeedQueryRepositoryImpl)
 */
public interface FeedQueryRepository {

  /**
   * 컨트롤러에서 쿼리 파라미터를 FeedSearchCond 객체로 변환하여 통계 조건으로 사용합니다.
   *
   * @param searchCond 쿼리 파라미터를 변환한 통계 조건 객체
   * @return 검색 조건에 맞는 게시물 리스트
   */
  public List<Feed> search(FeedSearchCond searchCond);
}
