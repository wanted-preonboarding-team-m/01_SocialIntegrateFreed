package com.wanted.socialintegratefreed.domain.feed.application;

import com.wanted.socialintegratefreed.domain.feed.constant.SearchType;
import com.wanted.socialintegratefreed.domain.feed.dao.FeedRepository;
import com.wanted.socialintegratefreed.domain.feed.dto.request.FeedCreateRequest;
import com.wanted.socialintegratefreed.domain.feed.dto.request.FeedSearchCond;
import com.wanted.socialintegratefreed.domain.feed.dto.request.FeedUpdateRequest;
import com.wanted.socialintegratefreed.domain.feed.dto.response.FeedDetailResponse;
import com.wanted.socialintegratefreed.domain.feed.dto.response.FeedSearchResponse;
import com.wanted.socialintegratefreed.domain.feed.entity.Feed;
import com.wanted.socialintegratefreed.domain.user.entity.User;
import com.wanted.socialintegratefreed.global.error.BusinessException;
import com.wanted.socialintegratefreed.global.error.ErrorCode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedService {

  // 외부 api 호출 메소드에서 좋아요와 공유를 구분하기 위한 상수
  private static final String LIKE = "likes";
  private static final String SHARE = "share";

  private final FeedRepository feedRepository;

  // 좋아요, 공유시 외부 api 호출을 위한 RestTemplate
  private final RestTemplate restTemplate;

  /**
   * 게시물 생성
   *
   * @param request 게시물 생성 dto
   * @param user 로그인 한 사용자
   * @return 생성된 게시글 Id
   */
  @Transactional
  public Long createFeed(FeedCreateRequest request, User user){
    Feed feed = feedRepository.save(request.toEntity(user));
    return feed.getFeedId();

  }

  /**
   * 게시물 수정
   *
   * @param feedId 수정할 게시글 Id
   * @param request 수정할 게시글 내용 dto
   */
  @Transactional
  public void updateFeed(Long feedId, FeedUpdateRequest request, User user){
    Feed feed = findFeedIdReturnFeed(feedId);
    feed.update(request.toEntity(feed));
  }

  /**
   * 게시물 삭제
   *
   * @param feedId 삭제할 게시글 Id
   */
  @Transactional
  public void deleteFeed(Long feedId){
    findFeedIdReturnFeed(feedId);
    feedRepository.deleteById(feedId);
  }

  /**
   * 게시물 상세 조회
   *
   * @param feedId 조회할 게시물 Id
   * @return 조회한 게시물 상세 내용
   */

  public FeedDetailResponse getFeedById(Long feedId) {
    return new FeedDetailResponse(findFeedIdReturnFeed(feedId));
  }

  /**
   * 게시물 id로 게시물 조회
   *
   * @param feedId 조회할 게시물 Id
   * @return Feed
   */
  public Feed findFeedIdReturnFeed(Long feedId){
    return feedRepository.findById(feedId)
        .orElseThrow(() -> new BusinessException(feedId, "feedId", ErrorCode.FEED_NOT_FOUND));
  }

  /**
   * 게시물 좋아요
   *
   * @param feedId 좋아요할 게시물 Id
   */
  public void like(Long feedId) {
    Feed feed = findFeedIdReturnFeed(feedId);

    // 게시물의 SNS 타입에 따라 다른 엔드포인트의 외부 api를 호출한다.
    callApi(feed, LIKE);

    // 게시물의 좋아요 수가 1 증가한다.
    feed.like();
  }

  /**
   * 게시물 공유
   *
   * @param feedId 공유할 게시물 Id
   */
  public void share(Long feedId) {
    Feed feed = findFeedIdReturnFeed(feedId);

    // 게시물의 SNS 타입에 따라 다른 엔드포인트의 외부 api를 호출한다.
    callApi(feed, SHARE);

    // 게시물의 공유 수가 1 증가한다.
    feed.share();
  }

  /**
   * 좋아요나 공유 요청이 들어왔을 경우, 지정된 SNS의 외부 api를 호출한다.
   *
   * @param feed 게시물
   * @param likeOrShare 요청이 좋아요인지 공유인지 확인
   */
  private void callApi(Feed feed, String likeOrShare) {
    // 게시물의 SNS 타입에 따라 다른 엔드포인트의 외부 api를 호출한다.
    String url = feed.getType().getUrl() + "/" + likeOrShare + "/" + feed.getFeedId();

    // 외부 api 호출 (현재 실제 동작은 구현 요구사항이 아니므로 반환 타입은 임시로 String.class로 둔다.)
    restTemplate.postForEntity(url, null, String.class);
  }

  /**
   * 검색 조건을 입력받아서 게시물 통계 결과 생성
   *
   * @param searchCond 검색 조건
   * @return 통계 결과
   */
  public FeedSearchResponse search(FeedSearchCond searchCond) {

    // 시작일과 종료일의 차이가 일자별 조회의 경우는 30일, 시간별 조회의 경우는 7일을 넘으면 예외 발생
    validateDate(searchCond.getStart(), searchCond.getEnd(), searchCond.getType());

    // 검색 조건에 부합하는 통계 결과 리스트 ([date] : [count]개, 예시: [2022-01-03] : 3개)
    List<String> searchResult = new ArrayList<>();

    // start, end, type에 맞춰서 기간 리스트를 생성
    List<LocalDateTime> dateList = createDateList(searchCond.getStart(), searchCond.getEnd(), searchCond.getType());

    // 각 기간마다 통계를 계산하고 결과 포맷에 맞추어 리스트에 추가
    for (LocalDateTime date : dateList) {
      Long count = feedRepository.search(date, searchCond);
      searchResult.add(formatResult(date, count));
    }

    return new FeedSearchResponse(searchResult);
  }

  /**
   * 시작일과 종료일 검증
   *
   * @param start 쿼리 파라미터로 입력받은 조회 시작일
   * @param end 쿼리 파라미터로 입력받은 조회 종료일
   * @param type 쿼리 파라미터로 입력받은 조회 타입
   */
  private void validateDate(LocalDateTime start, LocalDateTime end, SearchType type) {
    if (type == SearchType.DATE && start.plusDays(30).isBefore(end)) {
      throw new BusinessException(start, "start", ErrorCode.OVER_PERIOD);
    }
    if (type == SearchType.HOUR && start.plusDays(7).isBefore(end)) {
      throw new BusinessException(start, "start", ErrorCode.OVER_PERIOD);
    }
  }

  /**
   * start, end, type에 맞춰서 기간 리스트를 생성
   *
   * @param start 조회 기준 시작일
   * @param end 조회 기준 종료일
   * @param type 일자별, 시간별 조회 타입
   * @return 기간 리스트
   */
  private List<LocalDateTime> createDateList(LocalDateTime start, LocalDateTime end, SearchType type) {
    List<LocalDateTime> dateList = new ArrayList<>();

    // start와 end의 시,분,초를 0으로 초기화한다.
    start = start.withHour(0).withMinute(0).withSecond(0);
    end = end.withHour(0).withMinute(0).withSecond(0);
    LocalDateTime date = start;

    // DATE 타입이면 start ~ end 기간내 일자별로 LocalDateTime 리스트 생성
    if (type == SearchType.DATE) {
      while (date.isBefore(end)) {
        dateList.add(date);
        date = date.plusDays(1);
      }
      return dateList;
    }

    // HOUR 타입이면 start ~ end 기간내 1시간 간격으로 LocalDateTime 리스트 생성
    while (date.isBefore(end)) {
      dateList.add(date);
      date = date.plusHours(1);
    }
    return dateList;
  }

  /**
   * 통계 결과 포맷에 맞추어 문자열을 생성
   *
   * @param date 날짜
   * @param count 개수
   * @return 포맷에 맞춘 통계 결과 문자열
   */
  private String formatResult(LocalDateTime date, Long count) {
    return String.format("[%s] : %d개", date.toString().substring(0, 10), count);
  }

}
