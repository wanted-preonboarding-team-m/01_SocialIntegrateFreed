package com.wanted.socialintegratefreed.domain.feed.api;

import com.wanted.socialintegratefreed.domain.user.entity.User;
import com.wanted.socialintegratefreed.domain.feed.dto.request.FeedCreateRequest;
import com.wanted.socialintegratefreed.domain.feed.application.FeedService;
import com.wanted.socialintegratefreed.domain.user.application.UserService;
import com.wanted.socialintegratefreed.global.format.response.ApiResponse;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feeds")
public class FeedController {

  private final FeedService feedService;
  private final UserService userService;

  /**
   * 신규 게시물 생성 api
   *
   * @param request 게시글 생성을 위한 요청 데이터
   * @return HTTP 상태 코드 및 생성된 게시글 id를 포함한 응답
   */
  @PostMapping
  public ResponseEntity<ApiResponse> createFeed(@RequestBody @Valid FeedCreateRequest request){
    //사용자 조회
    User user = userService.getUserById(request.getUserId());

    //생성한 게시글 ID 반환
    Long createdFeedId = feedService.createFeed(request,user);

    //ApiResponse를 사용한 성공 응답 생성
    ApiResponse apiResponse = ApiResponse.toSuccessForm("게시글 생성 성공: " + createdFeedId);

    return ResponseEntity.status(HttpStatus.CREATED)
        .location(URI.create("/feeds/" + createdFeedId))
        .body(apiResponse);
  }
}
