package com.wanted.socialintegratefreed.domain.feed.api;

import com.wanted.socialintegratefreed.domain.feed.dto.request.FeedUpdateRequest;
import com.wanted.socialintegratefreed.domain.feed.dto.response.FeedDetailResponse;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
   * 게시물 수정 api
   *
   * @param feedId 수정할 게시물 Id
   * @param request
   * @return 200
   */
  @PutMapping("/{feedId}")
  public ResponseEntity<ApiResponse> updateFeed(@PathVariable Long feedId, @RequestBody @Valid FeedUpdateRequest request) {
    //사용자 조회
    User user = userService.getUserById(request.getUserId());

    //게시물 수정
    feedService.updateFeed(feedId, request, user);

    //ApiResponse를 사용한 성공 응답 생성
    ApiResponse apiResponse = ApiResponse.toSuccessForm("게시글 업데이트 성공: " + feedId);

    return ResponseEntity.ok(apiResponse);
  }

  /**
   * 게시물 삭제 api
   *
   * @param feedId 삭제할 게시물 Id
   * @return 200
   */
  @DeleteMapping("/{feedId}")
  public ResponseEntity<ApiResponse> deleteFeed(@PathVariable Long feedId) {
    //게시물 삭제
    feedService.deleteFeed(feedId);

    //ApiResponse를 사용한 성공 응답 생성
    ApiResponse apiResponse = ApiResponse.toSuccessForm("게시글 삭제 성공: " + feedId);

    return ResponseEntity.ok(apiResponse);
  }

}
