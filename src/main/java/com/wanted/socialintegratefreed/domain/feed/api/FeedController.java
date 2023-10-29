package com.wanted.socialintegratefreed.domain.feed.api;

import static com.wanted.socialintegratefreed.domain.feed.constant.SearchType.DATE;
import static com.wanted.socialintegratefreed.global.error.ErrorCode.INVALID_DATE;
import static com.wanted.socialintegratefreed.global.error.ErrorCode.INVALID_SEARCH_TYPE;

import com.wanted.socialintegratefreed.domain.feed.application.FeedService;
import com.wanted.socialintegratefreed.domain.feed.constant.SearchType;
import com.wanted.socialintegratefreed.domain.feed.constant.SearchValue;
import com.wanted.socialintegratefreed.domain.feed.dto.request.FeedCreateRequest;
import com.wanted.socialintegratefreed.domain.feed.dto.request.FeedSearchCond;
import com.wanted.socialintegratefreed.domain.feed.dto.request.FeedUpdateRequest;
import com.wanted.socialintegratefreed.domain.feed.dto.response.FeedDetailResponse;
import com.wanted.socialintegratefreed.domain.feed.dto.response.FeedSearchResponse;
import com.wanted.socialintegratefreed.domain.user.application.UserService;
import com.wanted.socialintegratefreed.domain.user.entity.User;
import com.wanted.socialintegratefreed.global.error.BusinessException;
import com.wanted.socialintegratefreed.global.error.ErrorCode;
import com.wanted.socialintegratefreed.global.format.response.ApiResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feeds")
public class FeedController {

  private final FeedService feedService;
  private final UserService userService;

  /**
   * 신규 게시물 생성 api
   *
   * @param request 게시글 생성을 위한 요청 데이터
   * @return 201 및 생성된 게시글 id
   */
  @PostMapping
  public ResponseEntity<ApiResponse> createFeed(
      @RequestBody @Valid FeedCreateRequest request
  ) {
    //사용자 조회
    User user = userService.findUserIdReturnUser(request.getUserId());

    //생성한 게시글 ID 반환
    Long createdFeedId = feedService.createFeed(request,user);

    //ApiResponse를 사용한 성공 응답 생성
    ApiResponse apiResponse = ApiResponse.toSuccessForm(createdFeedId);

    return ResponseEntity.status(HttpStatus.CREATED)
        .location(URI.create("/feeds/" + createdFeedId))
        .body(apiResponse);
  }

  /**
   * 게시물 수정 api
   *
   * @param feedId 수정할 게시물 Id
   * @param request
   * @return 200
   */
  @PutMapping("/{feedId}")
  public ResponseEntity<ApiResponse> updateFeed(
      @PathVariable Long feedId,
      @RequestBody @Valid FeedUpdateRequest request
  ) {
    //사용자 조회
    User user = userService.findUserIdReturnUser(request.getUserId());

    //게시물 수정
    feedService.updateFeed(feedId, request, user);

    //ApiResponse를 사용한 성공 응답 생성
    ApiResponse apiResponse = ApiResponse.toSuccessForm(feedId);

    return ResponseEntity.ok(ApiResponse.toSuccessForm(feedId));
  }

  /**
   * 게시물 삭제 api
   *
   * @param feedId 삭제할 게시물 Id
   * @return 200
   */
  @DeleteMapping("/{feedId}")
  public ResponseEntity<ApiResponse> deleteFeed(
      @PathVariable Long feedId
  ) {
    //게시물 삭제
    feedService.deleteFeed(feedId);

    return ResponseEntity.ok(ApiResponse.toSuccessForm(feedId));
  }

  /**
   * 게시물 상세 조회 api
   *
   * @param feedId 조회할 게시물 id
   * @return 200, 게시물 상세 내용
   */
  @GetMapping("/{feedId}")
  public ResponseEntity<ApiResponse> searchFeed(
      @PathVariable Long feedId
  ) {
    //게시물 조회
    FeedDetailResponse feedResponse = feedService.getFeedById(feedId);

    //ApiResponse를 사용한 성공 응답 생성
    ApiResponse apiResponse = ApiResponse.toSuccessForm(feedResponse);

    return ResponseEntity.ok(apiResponse);
  }

  /**
   * 게시물 통계 조회 api
   *
   * @param params 쿼리 파라미터
   * @return 200, 게시물 통계 결과
   */
  @GetMapping
  public ResponseEntity<ApiResponse> makeStatistic(@RequestParam Map<String, String> params) {

    // 쿼리 파라미터를 FeedSearchCond 객체로 변환
    FeedSearchCond searchCond = toFeedSearchCond(params);

    // 조건에 부합하는 통계 결과 생성
    FeedSearchResponse searchResponse = feedService.search(searchCond);

    // ApiResponse를 사용한 성공 응답 생성
    ApiResponse apiResponse = ApiResponse.toSuccessForm(searchResponse);

    return ResponseEntity.ok(apiResponse);
  }

  /**
   * 쿼리 파라미터를 FeedSearchCond 객체로 변환
   *
   * @param params 쿼리 파라미터
   * @return FeedSearchCond 객체
   */
  private FeedSearchCond toFeedSearchCond(Map<String, String> params) {
    return FeedSearchCond.builder()
        .hashtag(checkHashtag(params.get("hashtag"))) // hashtag 검증 및 null 일시 본인계정
        .type(toSearchType(params.get("type"))) // type 검증 및 enum으로 변환
        .start(toDate(params.get("start"), "start")) // start 검증 및 변환, null 일시 오늘로부터 7일 전
        .end(toDate(params.get("end"), "end")) // end 검증 및 변환, null 일시 오늘
        .value(toSearchValue(params.get("value"))) // value 검증 및 enum으로 변환
        .build();
  }

  /**
   * hashtag 검증 및 null 일시 본인계정으로 변환
   *
   * @param hashtag 쿼리 파라미터로 전달된 hashtag
   * @return hashtag
   */
  private String checkHashtag(String hashtag) {
    // hashtag가 null일 경우 본인계정으로 변환
    if (!StringUtils.hasText(hashtag)) {
      // 로그인한 사용자 정보 조회
      User user = findLoginUser();
      return user.getEmail();
    }
    return hashtag;
  }

  private User findLoginUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();
    return userService.existEmailReturnUser(email);
  }

  /**
   * 쿼리 파라미터로 전달된 type을 SearchType enum으로 변환
   * 입력이 date 혹은 hour가 아닐 경우 예외 발생
   *
   * @param type 쿼리 파라미터로 전달된 type
   * @return SearchType enum
   */
  private SearchType toSearchType(String type) {
    if (type.equals("date")) {
      return DATE;
    }
    if (type.equals("hour")) {
      return SearchType.HOUR;
    }
    throw new BusinessException(type, "type", INVALID_SEARCH_TYPE);
  }

  /**
   * 쿼리 파라미터로 전달된 날짜 형식을 검증 및 LocalDateTime으로 변환
   * 입력이 날짜 형식이 아닐 경우 예외 발생
   * 입력이 null일 경우 알맞은 날짜로 변환
   *
   * @param date 쿼리 파라미터로 전달된 날짜 형식
   * @param startOrEnd start인지 end인지
   * @return LocalDateTime
   */
  private LocalDateTime toDate(String date, String startOrEnd) {
    // 입력이 null일 경우 알맞은 날짜로 변환
    if (!StringUtils.hasText(date)) {
      if(startOrEnd.equals("start")) {
        return LocalDateTime.now().minusDays(7);
      }
      if (startOrEnd.equals("end")) {
        return LocalDateTime.now();
      }
    }

    // String 입력을 LocalDateTime으로 변환
    try {
      date += " 00:00:00"; // 날짜 형식에 맞춰 시간 추가
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 날짜 형식 Formatter
      return LocalDateTime.parse(date, formatter);
    } catch (DateTimeParseException exception) {
      throw new BusinessException(date, "date", INVALID_DATE);
    }
  }

  /**
   * 쿼리 파라미터로 전달된 value를 검증
   * 입력이 count, view_count, like_count, share_count가 아닐 경우 예외 발생
   *
   * @param value 쿼리 파라미터로 전달된 value
   * @return value
   */
  private SearchValue toSearchValue(String value) {

    // 게시물의 총 개수
    if (value.equals("count")) {
      return SearchValue.COUNT;
    }

    // 게시물의 조회수 합
    if (value.equals("view_count")) {
      return SearchValue.VIEW_COUNT;
    }

    // 게시물의 좋아요 개수 합
    if (value.equals("like_count")) {
      return SearchValue.LIKE_COUNT;
    }

    // 게시물의 공유수 합
    if (value.equals("share_count")) {
      return SearchValue.SHARE_COUNT;
    }

    // 입력이 count, view_count, like_count, share_count가 아닐 경우 예외 발생
    throw new BusinessException(value, "value", ErrorCode.INVALID_VALUE);
  }
}
