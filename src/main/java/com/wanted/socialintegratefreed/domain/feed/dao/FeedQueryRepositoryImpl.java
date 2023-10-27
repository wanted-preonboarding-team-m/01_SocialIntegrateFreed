package com.wanted.socialintegratefreed.domain.feed.dao;

import static com.wanted.socialintegratefreed.domain.feed.constant.SearchType.DATE;
import static com.wanted.socialintegratefreed.domain.feed.entity.QFeed.feed;
import static com.wanted.socialintegratefreed.domain.tagmatching.entity.QTagMatching.tagMatching;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanted.socialintegratefreed.domain.feed.constant.SearchType;
import com.wanted.socialintegratefreed.domain.feed.dto.request.FeedSearchCond;
import com.wanted.socialintegratefreed.global.error.BusinessException;
import com.wanted.socialintegratefreed.global.error.ErrorCode;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;

/**
 * FeedQueryRepository 인터페이스의 구현체
 * Querydsl을 사용하는 인터페이스는 이름 뒤에 Impl을 붙이면 자동으로 빈으로 등록됩니다.
 */
public class FeedQueryRepositoryImpl implements FeedQueryRepository {

  // Jpql을 생성하기 위한 EntityManager
  private final EntityManager em;

  // Querydsl을 사용하기 위한 JPAQueryFactory
  private final JPAQueryFactory query;

  public FeedQueryRepositoryImpl(EntityManager em) {
    this.em = em;
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
    return query
        .select(chooseCount(searchCond.getValue()))
        .from(feed)
        .join(feed.tagMatchings, tagMatching) // 반환 타입이 엔티티가 아니어서, fetch join을 적용하면 예외 발생
        .where(feed.tagMatchings.any().hashtag.name.eq(searchCond.getHashtag())) // 해시태그가 검색 조건과 일치하는 게시물만 검색
        .where(chooseInterval(date, searchCond.getType())) // 검색 기간이 일자별인지 시간별인지에 따라 검색
        .fetchOne();
  }

  /**
   * 게시물 개수, 조회수 합, 좋아요 합, 공유 수 합 중 어떤 통계를 원하는지 선택
   *
   * @param value count, view_count, like_count, share_count 중 하나
   * @return 어떤 개수를 조건으로 삼을지 반환
   */
  private NumberExpression<Long> chooseCount(String value) {
    switch (value) {
      // 게시물 개수
      case "count" -> {
        return feed.count();
      }
      // 조회수 합
      case "view_count" -> {
        return feed.viewCount.sum().longValue();
      }
      // 좋아요 합
      case "like_count" -> {
        return feed.likeCount.sum().longValue();
      }
      // 공유 수 합
      case "share_count" -> {
        return feed.shareCount.sum().longValue();
      }
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
