package com.wanted.socialintegratefreed.domain.feed.dao;

import static com.wanted.socialintegratefreed.domain.feed.constant.SearchType.DATE;
import static com.wanted.socialintegratefreed.domain.feed.entity.QFeed.feed;
import static com.wanted.socialintegratefreed.domain.tagmatching.entity.QTagMatching.tagMatching;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanted.socialintegratefreed.domain.feed.constant.SearchType;
import com.wanted.socialintegratefreed.domain.feed.constant.SearchValue;
import com.wanted.socialintegratefreed.domain.feed.dto.request.FeedSearchCond;
import com.wanted.socialintegratefreed.domain.feed.entity.Feed;
import com.wanted.socialintegratefreed.global.error.BusinessException;
import com.wanted.socialintegratefreed.global.error.ErrorCode;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

/**
 * FeedQueryRepository 인터페이스의 구현체
 * Querydsl을 사용하는 인터페이스는 이름 뒤에 Impl을 붙이면 자동으로 빈으로 등록됩니다.
 */
public class FeedQueryRepositoryImpl implements FeedQueryRepository {

  // Querydsl을 사용하기 위한 JPAQueryFactory
  private final JPAQueryFactory query;

  public FeedQueryRepositoryImpl(EntityManager em) {
    this.query = new JPAQueryFactory(em);
  }

  /**
   * 검색 조건에 부합하는 value의 개수를 센다.
   *
   * @param date 검색 기간
   * @param searchCond 검색 조건
   * @return 검색 조건에 부합하는 통계 결과
   */
  @Override
  public Long search(LocalDateTime date, FeedSearchCond searchCond) {
    List<Feed> feedList = query
        .select(feed)
        .from(feed)
        .join(feed.tagMatchings, tagMatching).fetchJoin() // fetch join을 적용하여 N + 1 문제 방지
        .where(feed.tagMatchings.any().hashtag.name.eq(
            searchCond.getHashtag())) // 해시태그가 검색 조건과 일치하는 게시물만 검색
        .where(chooseInterval(date, searchCond.getType())) // 검색 기간이 일자별인지 시간별인지에 따라 검색
        .fetch();

    return calculateCount(feedList, searchCond.getValue());
  }

  /**
   * 쿼리 파라미터로 입력받은 value 값에 따라, 게시물의 총 개수, 좋아요 개수의 합, 조회수 합, 공유수 합를 계산한다.
   *
   * @param feedList 검색 조건에 부합하는 게시물 리스트
   * @param value count, like_count, view_count, share_count 중 하나
   * @return 게시물의 총 개수, 좋아요 개수, 조회수, 공유수 중 하나
   */
  private Long calculateCount(List<Feed> feedList, SearchValue value) {

    // 게시물의 총 개수
    if (value == SearchValue.COUNT) {
      return (long) feedList.size();
    }

    // 게시물의 좋아요 개수의 합
    if (value == SearchValue.LIKE_COUNT) {
      return (long) feedList.stream()
          .map(Feed::getLikeCount)
          .reduce(Integer::sum).orElse(0);
    }

    // 게시물의 조회수 합
    if (value == SearchValue.VIEW_COUNT) {
      return (long) feedList.stream()
          .map(Feed::getViewCount)
          .reduce(Integer::sum).orElse(0);
    }

    // 게시물의 공유수 합
    if (value == SearchValue.SHARE_COUNT) {
      return (long) feedList.stream()
          .map(Feed::getShareCount)
          .reduce(Integer::sum).orElse(0);
    }

    throw new BusinessException(value, "value", ErrorCode.INVALID_VALUE);
  }

  /**
   * 검색 기간이 일자별인지 시간별인지에 따라 검색 조건을 다르게 준다.
   *
   * @param date 검색 기간
   * @param type 일자별, 시간별 조회 타입
   * @return 검색 조건
   */
  private BooleanExpression chooseInterval(LocalDateTime date, SearchType type) {
    // 일자별이면 1일 간격으로
    if (type == DATE) {
      return feed.createdAt.between(date, date.plusDays(1));
    }

    // 시간별이면 1시간 간격으로
    return feed.createdAt.between(date, date.plusHours(1));
  }
}
