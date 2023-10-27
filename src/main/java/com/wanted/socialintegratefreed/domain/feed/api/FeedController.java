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
   * 게시물 상세 조회 api
   *
   * @param feedId 조회할 게시물 id
   * @return 200, 게시물 상세 내용
   */
  @GetMapping("/{feedId}")
  public ResponseEntity<ApiResponse> searchFeed(@PathVariable Long feedId) {
    //게시물 조회
    FeedDetailResponse feedResponse = feedService.getFeedById(feedId);

    //ApiResponse를 사용한 성공 응답 생성
    ApiResponse apiResponse = ApiResponse.toSuccessForm(feedResponse);

    return ResponseEntity.ok(apiResponse);
  }
}
